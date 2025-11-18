package br.com.luangenro.menu.manager.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Represents the response sent after a category has been successfully created. It provides the
 * essential identifiers of the new resource.
 *
 * @param id The unique, system-generated identifier for the newly created category.
 * @param name The name of the category that was created.
 */
@Schema(description = "Response payload after successfully creating a category.")
@Builder
public record CreateCategoryResponse(
    @Schema(description = "The unique ID of the created category.", example = "1") Integer id,
    @Schema(description = "The name of the created category.", example = "Beverages")
        String name) {}
