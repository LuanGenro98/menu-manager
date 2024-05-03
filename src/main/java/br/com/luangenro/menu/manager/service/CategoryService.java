package br.com.luangenro.menu.manager.service;

import br.com.luangenro.menu.manager.domain.dto.CategoryResponse;
import br.com.luangenro.menu.manager.domain.dto.CreateCategoryRequest;
import br.com.luangenro.menu.manager.domain.dto.CreateCategoryResponse;
import br.com.luangenro.menu.manager.domain.dto.ItemResponse;
import br.com.luangenro.menu.manager.domain.entity.Category;
import br.com.luangenro.menu.manager.exception.CategoryNotFoundException;
import br.com.luangenro.menu.manager.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryResponse getCategory(int id){
        var category = repository.findById(id).orElse(null);
        if(isEmpty(category)){
            throw new CategoryNotFoundException("category not found");
        }
        return CategoryResponse
                .builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .items(category.getItems()
                        .stream()
                        .map(item -> ItemResponse
                                .builder()
                                .id(item.getId())
                                .name(item.getName())
                                .description(item.getDescription())
                                .price(item.getPrice())
                                .build())
                        .toList())
                .build();
    }

    public List<CategoryResponse> getCategories(){
        var categories = repository.findAll();
        if(isEmpty(categories)){
            throw new CategoryNotFoundException("categories not found");
        }
        return categories
                .stream()
                .map(category -> CategoryResponse
                        .builder()
                        .id(category.getId())
                        .name(category.getName())
                        .description(category.getDescription())
                        .items(category.getItems()
                                .stream()
                                .map(item -> ItemResponse
                                        .builder()
                                        .id(item.getId())
                                        .name(item.getName())
                                        .description(item.getDescription())
                                        .price(item.getPrice())
                                        .build())
                                .toList())
                        .build())
                .toList();
    }

    public CreateCategoryResponse createCategory(CreateCategoryRequest request) {
        var category = Category
                .builder()
                .uuid(UUID.randomUUID())
                .name(request.name())
                .description(request.description())
                .build();
        repository.save(category);
        return CreateCategoryResponse
                .builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
