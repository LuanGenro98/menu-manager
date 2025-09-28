package br.com.luangenro.menu.manager.service;

import br.com.luangenro.menu.manager.domain.dto.CreateDemandRequest;
import br.com.luangenro.menu.manager.domain.dto.CreateDemandResponse;
import br.com.luangenro.menu.manager.domain.entity.Demand;
import br.com.luangenro.menu.manager.domain.entity.DemandItem;
import br.com.luangenro.menu.manager.domain.entity.Item;
import br.com.luangenro.menu.manager.domain.enumeration.DemandStatus;
import br.com.luangenro.menu.manager.repository.DemandItemRepository;
import br.com.luangenro.menu.manager.repository.DemandRepository;
import br.com.luangenro.menu.manager.repository.ItemRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DemandService {

  private final DemandRepository demandRepository;
  private final DemandItemRepository demandItemRepository;
  private final ItemRepository itemRepository;

  public CreateDemandResponse createDemand(CreateDemandRequest demandRequest) {
    List<Item> demandedItems =
        demandRequest.itemsIds().stream()
            .map(id -> itemRepository.findById(id).orElse(null))
            .toList();

    var demand =
        Demand.builder()
            .uuid(UUID.randomUUID())
            .createdAt(Instant.now())
            .status(DemandStatus.ORDERED)
            .tableNumber(demandRequest.tableNumber())
            //            .price(demandedItems.stream().mapToDouble(item -> item.getPrice()).sum())
            .price(new BigDecimal(0))
            //TODO: Ajustar
            .build();

    demandRepository.save(demand);

    var demandItems =
        demandedItems.stream()
            .map(item -> DemandItem.builder().item(item).demand(demand).build())
            .toList();

    demandItemRepository.saveAll(demandItems);

    return new CreateDemandResponse(demand.getId(), demand.getTableNumber());
  }
}
