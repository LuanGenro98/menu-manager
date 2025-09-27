package br.com.luangenro.menu.manager.controller;

import br.com.luangenro.menu.manager.domain.dto.CategoryResponse;
import br.com.luangenro.menu.manager.domain.dto.CreateCategoryRequest;
import br.com.luangenro.menu.manager.domain.dto.CreateCategoryResponse;
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

/**
 * Defines the public contract for managing categories.
 *
 * <p>This interface contains all the endpoints related to category operations, serving as the
 * single source of truth for the API's capabilities and documentation.
 */
@Tag(name = "Categories", description = "Endpoints for managing product categories")
@RequestMapping("/api/v1/categories")
public interface CategoryApi {

  /**
   * Retrieves a specific category based on its unique identifier.
   *
   * @param id The unique identifier (ID) of the category to be found.
   * @return A {@link ResponseEntity} containing the {@link CategoryResponse} if found (HTTP 200),
   *     or an appropriate error response if not found (e.g., HTTP 404).
   */
  @Operation(summary = "Find a category by its ID")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Category found",
        content = {
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = CategoryResponse.class))
        }),
    @ApiResponse(
        responseCode = "404",
        description = "Category not found",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  @GetMapping("/{id}")
  ResponseEntity<CategoryResponse> getCategory(
      @Parameter(description = "ID of the category to be retrieved", required = true) @PathVariable
          Integer id);

  /**
   * Fetches a list of all available categories.
   *
   * @return A {@link ResponseEntity} with a list of {@link CategoryResponse} objects and an HTTP
   *     200 status.
   */
  @Operation(summary = "List all categories")
  @ApiResponse(responseCode = "200", description = "A list of all categories")
  @GetMapping
  ResponseEntity<List<CategoryResponse>> getCategories();

  /**
   * Creates a new category based on the provided data.
   *
   * @param request The request body containing the details for the new category. Must not be null
   *     and should be valid.
   * @return A {@link ResponseEntity} with the {@link CreateCategoryResponse} of the newly created
   *     category, a location header pointing to the new resource, and an HTTP 201 (Created) status.
   */
  @Operation(summary = "Create a new category")
  @ApiResponses({
    @ApiResponse(
        responseCode = "201",
        description = "Category created successfully",
        content = {
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = CreateCategoryResponse.class))
        }),
    @ApiResponse(
        responseCode = "400",
        description = "Invalid request body due to validation constraints",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  @PostMapping
  ResponseEntity<CreateCategoryResponse> createCategory(
      @RequestBody @Valid CreateCategoryRequest request);
}
