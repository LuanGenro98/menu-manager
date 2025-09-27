package br.com.luangenro.menu.manager.controller;

import br.com.luangenro.menu.manager.domain.dto.CategoryResponse;
import br.com.luangenro.menu.manager.domain.dto.CreateCategoryRequest;
import br.com.luangenro.menu.manager.domain.dto.CreateCategoryResponse;
import br.com.luangenro.menu.manager.service.CategoryService;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CategoryController implements CategoryApi {

  private final CategoryService service;
  private final String categoriesEndpoint;

  public CategoryController(
      CategoryService service,
      @Value("${menumanager.endpoints.categories}") String categoriesEndpoint) {
    this.service = service;
    this.categoriesEndpoint = categoriesEndpoint;
  }

  public ResponseEntity<CategoryResponse> getCategory(Integer id) {
    var response = service.getCategory(id);
    return ResponseEntity.ok(response);
  }

  public ResponseEntity<List<CategoryResponse>> getCategories() {
    var response = service.getCategories();
    return ResponseEntity.ok(response);
  }

  public ResponseEntity<CreateCategoryResponse> createCategory(CreateCategoryRequest request) {
    var response = service.createCategory(request);
    return ResponseEntity.created(URI.create(categoriesEndpoint + response.id())).body(response);
  }
}
