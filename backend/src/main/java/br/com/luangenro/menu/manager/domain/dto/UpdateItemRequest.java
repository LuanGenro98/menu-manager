package br.com.luangenro.menu.manager.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Represents the data required to update an existing menu item.
 *
 * @param name The new name for the item.
 * @param description The new short description for the item.
 * @param price The new price for the item.
 * @param categoryId The new category ID this item belongs to.
 */
@Schema(description = "Payload for updating an existing menu item.")
public record UpdateItemRequest(
    @Schema(
            description = "New name of the item.",
            example = "Classic Cheeseburger",
            requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 50)
        String name,
    @Schema(
            description = "New detailed description.",
            example = "A juicy 180g beef burger with melted cheddar cheese.",
            requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 100)
        String description,
    @Schema(
            description = "New price of the item.",
            example = "32.00",
            requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        @PositiveOrZero
        BigDecimal price,
    @Schema(
            description = "The new category ID this item belongs to.",
            example = "3",
            requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        @Positive
        int categoryId,
    @Schema(
            description = "URL of the item's image",
            example = "https://s3.amazonaws.com/bucket/uploads/file.png")
        String imageUrl) {}
