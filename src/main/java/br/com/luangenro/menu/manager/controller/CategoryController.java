package br.com.luangenro.menu.manager.controller;

import br.com.luangenro.menu.manager.domain.dto.CategoryResponse;
import br.com.luangenro.menu.manager.service.CategoryService;
import br.com.luangenro.menu.manager.domain.dto.CreateCategoryRequest;
import br.com.luangenro.menu.manager.domain.dto.CreateCategoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @Value("${menumanager.endpoints.categories}")
    private String CATEGORIES_ENDPOINT;

    @GetMapping("{id}")
    ResponseEntity<CategoryResponse> getCategory(@PathVariable Integer id){
        var response = service.getCategory(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    ResponseEntity<List<CategoryResponse>> getCategories(){
        var response = service.getCategories();
        return ResponseEntity.ok(response);
    }

    @PostMapping("create")
    ResponseEntity<CreateCategoryResponse> createCategory(@RequestBody @Valid CreateCategoryRequest request){
        var response = service.createCategory(request);
        return ResponseEntity.created(
                URI.create(CATEGORIES_ENDPOINT + response.uuid()))
                .body(response);
    }
}
