package br.com.luangenro.menu.manager.controller;

import br.com.luangenro.menu.manager.domain.dto.CreateItemRequest;
import br.com.luangenro.menu.manager.domain.dto.CreateItemResponse;
import br.com.luangenro.menu.manager.domain.dto.ItemResponse;
import br.com.luangenro.menu.manager.domain.dto.UpdateItemRequest;
import br.com.luangenro.menu.manager.exception.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Defines the public contract for managing menu items.
 *
 * <p>This interface centralizes all API definitions for item-related operations.
 */
@Tag(name = "Items", description = "Endpoints for creating and managing menu items")
@RequestMapping("/api/v1/items")
public interface ItemApi {

  /**
   * Retrieves a specific item by its unique identifier.
   *
   * @param id The unique identifier (ID) of the item to be found.
   * @return A {@link ResponseEntity} containing the {@link ItemResponse} if found (HTTP 200), or an
   *     appropriate error response if not found (e.g., HTTP 404).
   */
  @Operation(summary = "Find an item by its ID")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Item found"),
    @ApiResponse(
        responseCode = "404",
        description = "Item not found",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  @GetMapping("/{id}")
  ResponseEntity<ItemResponse> getItem(
      @Parameter(description = "ID of the item to be retrieved", required = true) @PathVariable
          int id);

  /**
   * Fetches a list of all available items, optionally filtered by category. If no categoryId is
   * provided, all items from all categories will be returned.
   *
   * @param categoryId (Optional) The ID of the category to filter items by.
   * @return A {@link ResponseEntity} with a list of {@link ItemResponse} objects.
   */
  @Operation(
      summary = "List all items",
      description = "Lists all available items. Can be filtered by category ID.")
  @ApiResponse(responseCode = "200", description = "A list of items")
  @GetMapping
  ResponseEntity<List<ItemResponse>> getItems(
      @Parameter(description = "Optional ID of the category to filter results")
          @RequestParam(required = false)
          Integer categoryId);

  /**
   * Updates an existing menu item.
   *
   * @param id The ID of the item to update.
   * @param request The request body with the new item details.
   * @return A {@link ResponseEntity} with the updated {@link ItemResponse}.
   */
  @Operation(summary = "Update an existing item")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Item updated successfully",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ItemResponse.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Invalid request data",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "404",
        description = "Item or Category not found",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  @PutMapping(
      value = "/{id}",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<ItemResponse> updateItem(
      @Parameter(description = "ID of the item to be updated", required = true) @PathVariable
          int id,
      @RequestPart("request") @Valid UpdateItemRequest request,
      @RequestPart(name = "image", required = false) MultipartFile image);

  /**
   * Deletes a menu item by its ID.
   *
   * @param id The ID of the item to delete.
   * @return A {@link ResponseEntity} with no content (HTTP 204).
   */
  @Operation(summary = "Delete an item")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Item deleted successfully"),
    @ApiResponse(
        responseCode = "404",
        description = "Item not found",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteItem(
      @Parameter(description = "ID of the item to be deleted", required = true) @PathVariable
          int id);

  /**
   * Creates a new menu item with an image upload.
   *
   * <p>This endpoint accepts multipart/form-data with: - data: JSON payload for CreateItemRequest -
   * image: the uploaded image file
   */
  @Operation(summary = "Create a new item with image")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Item created successfully"),
    @ApiResponse(
        responseCode = "400",
        description = "Invalid request or image upload error",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  @PostMapping(
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<CreateItemResponse> createItemWithImage(
      @RequestPart("request") @Valid CreateItemRequest request,
      @RequestPart(name = "image", required = false) MultipartFile image);
}
