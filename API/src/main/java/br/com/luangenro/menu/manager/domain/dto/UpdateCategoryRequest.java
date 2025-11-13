package br.com.luangenro.menu.manager.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Represents the data required to update an existing category.
 *
 * @param name The new name for the category.
 * @param description The new description for the category.
 */
@Schema(description = "Data required to update an existing menu category.")
public record UpdateCategoryRequest(
    @Schema(
            description = "The new name of the category.",
            example = "Appetizers",
            requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 50)
        String name,
    @Schema(
            description = "The new description for the category.",
            example = "Starters and finger foods.",
            requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 100)
        String description) {}
