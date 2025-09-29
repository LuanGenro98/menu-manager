package br.com.luangenro.menu.manager.service;

import br.com.luangenro.menu.manager.domain.dto.CreateDemandRequest;
import br.com.luangenro.menu.manager.domain.dto.CreateDemandResponse;
import br.com.luangenro.menu.manager.domain.entity.Demand;
import br.com.luangenro.menu.manager.domain.entity.DemandItem;
import br.com.luangenro.menu.manager.domain.entity.Item;
import br.com.luangenro.menu.manager.domain.enumeration.DemandStatus;
import br.com.luangenro.menu.manager.exception.ItemNotFoundException;
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

    // 📌 MELHORIA: Valida se todos os itens existem antes de prosseguir
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

    // 📌 MELHORIA: Calcula o preço total do pedido
    BigDecimal totalPrice =
        demandedItems.stream().map(Item::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    log.debug("Calculated total price for the demand: {}", totalPrice);

    var demand =
        Demand.builder()
            .uuid(UUID.randomUUID())
            .createdAt(Instant.now())
            .status(DemandStatus.ORDERED)
            .tableNumber(demandRequest.tableNumber())
            .price(totalPrice) // Usa o preço calculado
            .build();

    log.debug("Demand entity to be saved: {}", demand);
    Demand savedDemand = demandRepository.save(demand);

    // Cria a lista de itens do pedido (tabela de junção)
    var demandItems =
        demandedItems.stream()
            .map(item -> DemandItem.builder().item(item).demand(savedDemand).build())
            .collect(Collectors.toList());

    // Salva todos os itens do pedido. O JpaRepository é otimizado para isso.
    // O DemandItemRepository não é mais necessário aqui, o Spring gerencia o saveAll
    // através do relacionamento, mas mantê-lo injetado é uma opção.
    savedDemand.setDemandItems(demandItems);
    demandRepository.save(savedDemand);

    log.info(
        "Demand {} created successfully for table {} with a total price of {}.",
        savedDemand.getId(),
        savedDemand.getTableNumber(),
        savedDemand.getPrice());

    return new CreateDemandResponse(savedDemand.getId(), savedDemand.getTableNumber());
  }
}
