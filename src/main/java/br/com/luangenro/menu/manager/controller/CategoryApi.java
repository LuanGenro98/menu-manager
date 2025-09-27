package br.com.luangenro.menu.manager.controller;

import br.com.luangenro.menu.manager.domain.dto.CategoryResponse;
import br.com.luangenro.menu.manager.domain.dto.CreateCategoryRequest;
import br.com.luangenro.menu.manager.domain.dto.CreateCategoryResponse;
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

@Tag(name = "Categories", description = "Endpoints for managing product categories")
@RequestMapping("/api/v1/categories")
public interface CategoryApi {

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
    @ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
  })
  @GetMapping("/{id}")
  ResponseEntity<CategoryResponse> getCategory(
      @Parameter(description = "ID of the category to be retrieved", required = true) @PathVariable
          Integer id);

  @Operation(summary = "List all categories")
  @ApiResponse(responseCode = "200", description = "A list of all categories")
  @GetMapping
  ResponseEntity<List<CategoryResponse>> getCategories();

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
    @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content)
  })
  @PostMapping
  ResponseEntity<CreateCategoryResponse> createCategory(
      @RequestBody @Valid CreateCategoryRequest request);
}
