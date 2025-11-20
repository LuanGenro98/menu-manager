package br.com.luangenro.menu.manager.mapper;

import br.com.luangenro.menu.manager.domain.dto.ItemResponse;
import br.com.luangenro.menu.manager.domain.entity.Item;
import org.springframework.stereotype.Component;

/** A component responsible for mapping Item entities to DTOs. */
@Component
public class ItemMapper {

  /**
   * Converts an {@link Item} entity to an {@link ItemResponse} DTO.
   *
   * @param item The item entity to be converted. Must not be null.
   * @return The corresponding {@code ItemResponse} DTO.
   */
  public ItemResponse toItemResponse(Item item) {
      boolean hasOrders = item.getDemandItems() != null && !item.getDemandItems().isEmpty();

      return ItemResponse.builder()
        .id(item.getId())
        .name(item.getName())
        .description(item.getDescription())
        .price(item.getPrice())
        .imageUrl(item.getImageUrl())
<<<<<<< HEAD
            .categoryId(item.getCategory() != null ? item.getCategory().getId() : null)
              .hasOrders(hasOrders)
=======
>>>>>>> 7e57ed6b904f64a89959525d9a234fa0ca3424dd
        .build();
  }
}
