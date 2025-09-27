package br.com.luangenro.menu.manager.controller;

import br.com.luangenro.menu.manager.domain.dto.CategoryResponse;
import br.com.luangenro.menu.manager.domain.dto.CreateCategoryRequest;
import br.com.luangenro.menu.manager.domain.dto.CreateCategoryResponse;
import br.com.luangenro.menu.manager.service.CategoryService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Controller implementation for the Category API.
 *
 * <p>This class handles the HTTP requests and delegates the business logic to the {@link
 * CategoryService}.
 */
@RestController
@RequiredArgsConstructor
public class CategoryController implements CategoryApi {

  private final CategoryService service;

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<CategoryResponse> getCategory(Integer id) {
    var response = service.getCategory(id);
    return ResponseEntity.ok(response);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<List<CategoryResponse>> getCategories() {
    var response = service.getCategories();
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
    var response = service.createCategory(request);

    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(response.id())
            .toUri();

    return ResponseEntity.created(location).body(response);
  }
}
