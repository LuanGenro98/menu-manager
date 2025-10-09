package br.com.luangenro.menu.manager.service;

import br.com.luangenro.menu.manager.domain.dto.CreateDemandRequest;
import br.com.luangenro.menu.manager.domain.dto.CreateDemandResponse;
import br.com.luangenro.menu.manager.domain.dto.DemandResponse;
import br.com.luangenro.menu.manager.domain.dto.UpdateDemandStatusRequest;
import br.com.luangenro.menu.manager.domain.entity.Demand;
import br.com.luangenro.menu.manager.domain.entity.DemandItem;
import br.com.luangenro.menu.manager.domain.entity.Item;
import br.com.luangenro.menu.manager.domain.enumeration.DemandStatus;
import br.com.luangenro.menu.manager.exception.DemandNotFoundException;
import br.com.luangenro.menu.manager.exception.ItemNotFoundException;
import br.com.luangenro.menu.manager.mapper.DemandMapper;
import br.com.luangenro.menu.manager.repository.DemandItemRepository;
import br.com.luangenro.menu.manager.repository.DemandRepository;
import br.com.luangenro.menu.manager.repository.ItemRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DemandService {

  private final DemandRepository demandRepository;
  private final ItemRepository itemRepository;
  private final DemandMapper mapper;
  private final DemandItemRepository demandItemRepository;

  /**
   * Creates a new customer demand based on the provided request data.
   *
   * <p>This method orchestrates the entire process of creating a new demand, which includes several
   * key steps:
   *
   * <ol>
   *   <li><b>Item Validation:</b> It first validates that every single item ID provided in the
   *       request corresponds to an existing item in the database. If any item is not found, the
   *       entire operation is aborted.
   *   <li><b>Price Calculation:</b> It calculates the total price of the demand by summing the
   *       prices of all validated items.
   *   <li><b>Demand Persistence:</b> It creates and saves the main {@link Demand} entity with the
   *       calculated total price and initial status.
   *   <li><b>Demand Items Persistence:</b> It then creates and saves the associated {@link
   *       DemandItem} entities, effectively linking the demand to its constituent items in the join
   *       table.
   * </ol>
   *
   * The entire operation is transactional. If any step fails (e.g., an item is not found or a
   * database error occurs), all changes are rolled back to ensure data integrity.
   *
   * @param demandRequest The DTO containing the list of item IDs and the table number for the new
   *     demand. Must not be null.
   * @return A {@link CreateDemandResponse} containing the unique ID and table number of the newly
   *     created demand.
   * @throws ItemNotFoundException if any of the item IDs included in the {@code demandRequest} do
   *     not exist in the database.
   */
  @Transactional
  public CreateDemandResponse createDemand(CreateDemandRequest demandRequest) {
    log.info(
        "Attempting to create a new demand for table {} with {} items.",
        demandRequest.tableNumber(),
        demandRequest.itemsIds().size());
    log.debug("Received item IDs for new demand: {}", demandRequest.itemsIds());

    List<Item> demandedItems = new ArrayList<>();
    for (Integer itemId : demandRequest.itemsIds()) {
      Item item =
          itemRepository
              .findById(itemId)
              .orElseThrow(
                  () ->
                      new ItemNotFoundException(
                          "Cannot create demand. Item with ID %d not found.".formatted(itemId)));
      demandedItems.add(item);
    }
    log.debug("Successfully fetched {} items from the database.", demandedItems.size());

    BigDecimal totalPrice =
        demandedItems.stream().map(Item::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    log.debug("Calculated total price for the demand: {}", totalPrice);

    var demand =
        Demand.builder()
            .uuid(UUID.randomUUID())
            .createdAt(Instant.now())
            .status(DemandStatus.RECEBIDO)
            .tableNumber(demandRequest.tableNumber())
            .price(totalPrice)
            .build();

    log.debug("Demand entity to be saved: {}", demand);
    Demand savedDemand = demandRepository.save(demand);

    var demandItems =
        demandedItems.stream()
            .map(item -> DemandItem.builder().item(item).demand(savedDemand).build())
            .collect(Collectors.toList());

    demandItemRepository.saveAll(demandItems);

    log.info(
        "Demand {} created successfully for table {} with a total price of {}.",
        savedDemand.getId(),
        savedDemand.getTableNumber(),
        savedDemand.getPrice());

    return new CreateDemandResponse(savedDemand.getId(), savedDemand.getTableNumber());
  }

  /**
   * Retrieves a single demand by its ID.
   *
   * @param id The demand ID.
   * @return A DTO representing the demand.
   * @throws DemandNotFoundException if the demand does not exist.
   */
  public DemandResponse getDemand(int id) {
    log.info("Fetching demand with ID: {}", id);
    Demand demand =
        demandRepository
            .findByIdWithItems(id)
            .orElseThrow(
                () -> new DemandNotFoundException("Demand with ID %d not found.".formatted(id)));
    return mapper.toDemandResponse(demand);
  }

  /**
   * Retrieves all demands, optionally filtering by status.
   *
   * @param status An optional status to filter by.
   * @return A list of demand DTOs.
   */
  public List<DemandResponse> getAllDemands(DemandStatus status) {
    List<Demand> demands;
    if (status != null) {
      log.info("Fetching all demands with status: {}", status);
      demands = demandRepository.findByStatus(status);
    } else {
      log.info("Fetching all demands.");
      demands = demandRepository.findAllWithItems();
    }
    log.info("Found {} demands.", demands.size());
    return demands.stream().map(mapper::toDemandResponse).toList();
  }

  /**
   * Updates the status of a specific demand.
   *
   * @param id The ID of the demand to update.
   * @param request The request with the new status.
   * @return A DTO of the updated demand.
   * @throws DemandNotFoundException if the demand does not exist.
   */
  @Transactional
  public DemandResponse updateDemandStatus(int id, UpdateDemandStatusRequest request) {
    log.info("Attempting to update status of demand ID {} to '{}'.", id, request.newStatus());
    Demand demand =
        demandRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new DemandNotFoundException(
                        "Cannot update. Demand with ID %d not found.".formatted(id)));

    demand.setStatus(request.newStatus());
    demand.setUpdatedAt(Instant.now());

    Demand updatedDemand = demandRepository.save(demand);
    log.info("Status of demand ID {} updated successfully to '{}'.", id, updatedDemand.getStatus());

    return mapper.toDemandResponse(updatedDemand);
  }
}
