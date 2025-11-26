package br.com.luangenro.menu.manager.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Represents the data required to create a new menu item.
 *
 * @param name The name of the item.
 * @param description A short description of the item.
 * @param price The price of the item. Using BigDecimal is recommended for monetary values.
 * @param categoryId The ID of the category this item belongs to.
 */
@Schema(description = "Payload for creating a new menu item.")
public record CreateItemRequest(
    @Schema(
            description = "Name of the item.",
            example = "Cheeseburger",
            requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 50)
        String name,
    @Schema(
            description = "Detailed description of the item.",
            example = "A classic beef burger with cheese, lettuce, and special sauce.",
            requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 100)
        String description,
    @Schema(
            description = "Price of the item.",
            example = "25.50",
            requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        @PositiveOrZero
        BigDecimal price,
    @Schema(
            description = "The ID of the category this item belongs to.",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        @Positive
        Integer categoryId,
    @Schema(
            description = "URL of the item's image",
            example = "https://s3.amazonaws.com/bucket/uploads/file.png")
        String imageUrl) {}
