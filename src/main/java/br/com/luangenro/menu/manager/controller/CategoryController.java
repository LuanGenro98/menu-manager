package br.com.luangenro.menu.manager.controller;

import br.com.luangenro.menu.manager.domain.dto.CategoryResponse;
import br.com.luangenro.menu.manager.domain.dto.CreateCategoryRequest;
import br.com.luangenro.menu.manager.domain.dto.CreateCategoryResponse;
import br.com.luangenro.menu.manager.domain.dto.UpdateCategoryRequest;
import br.com.luangenro.menu.manager.service.CategoryService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Controller implementation for the Category API. This class handles the HTTP requests and
 * delegates the business logic to the {@link CategoryService}.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class CategoryController implements CategoryApi {

  private final CategoryService service;

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<CategoryResponse> getCategory(Integer id) {
    log.info("Received request to get category with ID: {}", id);
    var response = service.getCategory(id);
    log.info("Responding with category ID {} found.", id);
    return ResponseEntity.ok(response);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<List<CategoryResponse>> getCategories() {
    log.info("Received request to get all categories.");
    var response = service.getCategories();
    log.info("Responding with a list of {} categories.", response.size());
    return ResponseEntity.ok(response);
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation builds the 'Location' header URI for the newly created resource using
   * Spring's {@code ServletUriComponentsBuilder} for robustness.
   */
  @Override
  public ResponseEntity<CreateCategoryResponse> createCategory(CreateCategoryRequest request) {
    log.info("Received request to create new category with name: '{}'", request.name());
    var response = service.createCategory(request);

    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(response.id())
            .toUri();

    log.info(
        "Category created with ID {}. Responding with 201 Created at location: {}",
        response.id(),
        location);
    return ResponseEntity.created(location).body(response);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<CategoryResponse> updateCategory(int id, UpdateCategoryRequest request) {
    log.info("Received request to update category with ID: {}", id);
    CategoryResponse updatedCategory = service.updateCategory(id, request);
    log.info("Category with ID {} updated successfully. Responding with 200 OK.", id);
    return ResponseEntity.ok(updatedCategory);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<Void> deleteCategory(int id) {
    log.info("Received request to delete category with ID: {}", id);
    service.deleteCategory(id);
    log.info("Category with ID {} deleted successfully. Responding with 204 No Content.", id);
    return ResponseEntity.noContent().build();
  }
}
