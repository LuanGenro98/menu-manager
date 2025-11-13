package br.com.luangenro.menu.manager.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

/**
 * Represents the full details of a category, including its associated menu items. This is the
 * primary DTO used for retrieving category information.
 *
 * @param id The unique identifier for the category.
 * @param name The name of the category.
 * @param description A brief description of the category.
 * @param items A list of menu items that belong to this category.
 */
@Schema(description = "Represents a single category with its list of menu items.")
@Builder
public record CategoryResponse(
    @Schema(description = "Unique ID of the category.", example = "1") Integer id,
    @Schema(description = "Name of the category.", example = "Beverages") String name,
    @Schema(
            description = "Description of the category.",
            example = "Soft drinks, juices, and teas.")
        String description,
    @Schema(description = "List of items within this category.") List<ItemResponse> items) {}
