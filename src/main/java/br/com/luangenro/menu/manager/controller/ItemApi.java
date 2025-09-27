package br.com.luangenro.menu.manager.controller;

import br.com.luangenro.menu.manager.domain.dto.CreateItemRequest;
import br.com.luangenro.menu.manager.domain.dto.CreateItemResponse;
import br.com.luangenro.menu.manager.domain.dto.ItemResponse;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                mediaType = "application/json",
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
   * Creates a new menu item.
   *
   * @param request The request body containing the details for the new item.
   * @return A {@link ResponseEntity} with the {@link CreateItemResponse}, a location header, and an
   *     HTTP 201 (Created) status.
   */
  @Operation(summary = "Create a new menu item")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Item created successfully"),
    @ApiResponse(
        responseCode = "400",
        description =
            "Invalid request data, such as a validation error or a non-existent category ID",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  @PostMapping
  ResponseEntity<CreateItemResponse> createItem(@RequestBody @Valid CreateItemRequest request);
}
