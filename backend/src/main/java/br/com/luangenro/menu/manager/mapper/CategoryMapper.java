package br.com.luangenro.menu.manager.mapper;

import br.com.luangenro.menu.manager.domain.dto.CategoryResponse;
import br.com.luangenro.menu.manager.domain.dto.ItemResponse;
import br.com.luangenro.menu.manager.domain.entity.Category;
import org.springframework.stereotype.Component;

/**
 * A component responsible for mapping Category entities to DTOs.
 *
 * <p>This class encapsulates the conversion logic, promoting clean architecture and separation of
 * concerns within the service layer.
 */
@Component
public class CategoryMapper {

  /**
   * Converts a {@link Category} entity to a {@link CategoryResponse} DTO.
   *
   * <p>This method maps the category's primary fields and also transforms the associated list of
   * {@code Item} entities into {@code ItemResponse} DTOs.
   *
   * @param category The category entity to be converted. Must not be null.
   * @return The corresponding {@code CategoryResponse} DTO.
   */
  public CategoryResponse toCategoryResponse(Category category) {
    return CategoryResponse.builder()
        .id(category.getId())
        .name(category.getName())
        .description(category.getDescription())
        .items(
            category.getItems().stream()
                .map(
                    item ->
                        ItemResponse.builder()
                            .id(item.getId())
                            .name(item.getName())
                            .description(item.getDescription())
                            .price(item.getPrice())
                            .imageUrl(item.getImageUrl())
                            .build())
                .toList())
        .build();
  }
}
