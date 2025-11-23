package br.com.luangenro.menu.manager.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Represents the data required to create a new category. This record is used as the request body
 * for category creation endpoints.
 *
 * @param name The name of the category (e.g., "Desserts", "Beverages"). Cannot be blank.
 * @param description A brief description of the category. Cannot be blank.
 */
@Schema(description = "Data required to create a new menu category.")
public record CreateCategoryRequest(
    @Schema(
            description = "The name of the category.",
            example = "Hamburgers",
            requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 50)
        String name,
    @Schema(
            description = "A short description of what the category includes.",
            example = "Hamburgers with various toppings and sides.",
            requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 100)
        String description) {}
