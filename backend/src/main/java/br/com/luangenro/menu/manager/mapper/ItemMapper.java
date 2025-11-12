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
    return ItemResponse.builder()
        .id(item.getId())
        .name(item.getName())
        .description(item.getDescription())
        .price(item.getPrice())
        .build();
  }
}
