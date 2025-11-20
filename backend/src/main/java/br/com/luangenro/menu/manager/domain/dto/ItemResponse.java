package br.com.luangenro.menu.manager.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Builder;

/**
 * Represents a single menu item.
 *
 * @param id The unique identifier for the item.
 * @param name The name of the item (e.g., "Coca-Cola").
 * @param description A brief description of the item (e.g., "350ml can").
 * @param price The price of the item.
 */
@Schema(description = "Represents a single menu item available for sale.")
@Builder
public record ItemResponse(
    @Schema(description = "Unique ID of the item.", example = "101") Integer id,
    @Schema(description = "Name of the item.", example = "Coca-Cola") String name,
    @Schema(description = "Description of the item.", example = "350ml can") String description,
    @Schema(description = "Price of the item.", example = "5.50") BigDecimal price,
    @Schema(
            description = "URL of the item's image",
            example = "https://s3.amazonaws.com/bucket/uploads/file.png")
        String imageUrl) {}
