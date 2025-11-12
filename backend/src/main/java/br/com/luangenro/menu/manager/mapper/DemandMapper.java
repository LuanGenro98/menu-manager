package br.com.luangenro.menu.manager.mapper;

import br.com.luangenro.menu.manager.domain.dto.DemandItemResponse;
import br.com.luangenro.menu.manager.domain.dto.DemandResponse;
import br.com.luangenro.menu.manager.domain.entity.Demand;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/** A component responsible for mapping Demand entities to DTOs. */
@Component
public class DemandMapper {

  /**
   * Converts a {@link Demand} entity to a {@link DemandResponse} DTO.
   *
   * @param demand The demand entity to be converted.
   * @return The corresponding {@code DemandResponse} DTO.
   */
  public DemandResponse toDemandResponse(Demand demand) {
    var itemsResponse =
        demand.getDemandItems().stream()
            .map(
                demandItem ->
                    new DemandItemResponse(
                        demandItem.getItem().getName(),
                        demandItem.getItem().getPrice())) // Assumes price is stored on the item
            .collect(Collectors.toList());

    return new DemandResponse(
        demand.getId(),
        demand.getTableNumber(),
        demand.getStatus(),
        demand.getPrice(),
        demand.getCreatedAt(),
        itemsResponse);
  }
}
