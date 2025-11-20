package br.com.luangenro.menu.manager.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Represents the response after a new item has been successfully created.
 *
 * @param id The unique identifier for the newly created item.
 * @param name The name of the item that was created.
 * @param imageUrl Public URL of the uploaded item image.
 */
@Schema(description = "Response payload after successfully creating an item.")
public record CreateItemResponse(
    @Schema(description = "The unique ID of the created item.", example = "105") Integer id,
    @Schema(description = "The name of the created item.", example = "Cheeseburger") String name,
    @Schema(
            description = "URL of the item's image.",
            example = "https://my-bucket.s3.sa-east-1.amazonaws.com/uploads/abc123.png")
        String imageUrl) {}
