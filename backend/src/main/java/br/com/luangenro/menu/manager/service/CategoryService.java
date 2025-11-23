package br.com.luangenro.menu.manager.service;

import br.com.luangenro.menu.manager.domain.dto.CategoryResponse;
import br.com.luangenro.menu.manager.domain.dto.CreateCategoryRequest;
import br.com.luangenro.menu.manager.domain.dto.CreateCategoryResponse;
import br.com.luangenro.menu.manager.domain.dto.UpdateCategoryRequest;
import br.com.luangenro.menu.manager.domain.entity.Category;
import br.com.luangenro.menu.manager.exception.CategoryNotFoundException;
import br.com.luangenro.menu.manager.mapper.CategoryMapper;
import br.com.luangenro.menu.manager.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static br.com.luangenro.menu.manager.domain.CacheName.GET_ALL_CATEGORIES;
import static br.com.luangenro.menu.manager.domain.CacheName.GET_CATEGORY_BY_ID;

/** Service layer responsible for business logic related to categories. */
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

  private final CategoryRepository repository;
  private final CategoryMapper mapper;

  /**
   * Retrieves a single category by its ID.
   *
   * @param id The unique identifier of the category.
   * @return A {@link CategoryResponse} containing the category details.
   * @throws CategoryNotFoundException if no category with the given ID is found.
   */
  @Cacheable(value = GET_CATEGORY_BY_ID, key = "#id")
  public CategoryResponse getCategory(int id) {
    log.info("Fetching category with ID: {}", id);
    Category category =
        repository
            .findById(id)
            .orElseThrow(
                () ->
                    new CategoryNotFoundException("Category with ID %d not found.".formatted(id)));
    log.info("Found category with ID {} and name '{}'.", id, category.getName());
    return mapper.toCategoryResponse(category);
  }

  /**
   * Retrieves a list of all categories.
   *
   * @return A list of {@link CategoryResponse}. Returns an empty list if no categories are found.
   */
  @Cacheable(value = GET_ALL_CATEGORIES, key = "'allCategories'")
  public List<CategoryResponse> getCategories() {
    log.info("Fetching all categories.");
    List<Category> categories = repository.findAll();
    log.info("Found {} categories.", categories.size());
    return categories.stream().map(mapper::toCategoryResponse).toList();
  }

  /**
   * Creates a new category based on the provided request data.
   *
   * @param request The DTO containing the data for the new category.
   * @return A {@link CreateCategoryResponse} with the ID and name of the newly created category.
   */
  @Transactional
  @CacheEvict(value = GET_ALL_CATEGORIES, key = "'allCategories'")
  public CreateCategoryResponse createCategory(CreateCategoryRequest request) {
    log.info("Attempting to create a new category with name: {}", request.name());
    var category =
        Category.builder()
            .uuid(UUID.randomUUID())
            .name(request.name())
            .description(request.description())
            .build();

    var savedCategory = repository.save(category);
    log.info(
        "Category '{}' created successfully with ID: {}",
        savedCategory.getName(),
        savedCategory.getId());

    return CreateCategoryResponse.builder()
        .id(savedCategory.getId())
        .name(savedCategory.getName())
        .build();
  }

  /**
   * Updates an existing category with new data.
   *
   * @param id The ID of the category to update.
   * @param request The DTO containing the new data.
   * @return A {@link CategoryResponse} representing the updated state of the category.
   * @throws CategoryNotFoundException if the category with the given ID does not exist.
   */
  @Transactional
  @Caching(evict = {
          @CacheEvict(value = GET_CATEGORY_BY_ID, key = "#id"),
          @CacheEvict(value = GET_ALL_CATEGORIES, key = "'allCategories'")
  })
  public CategoryResponse updateCategory(int id, UpdateCategoryRequest request) {
    log.info("Attempting to update category with ID: {}", id);
    Category categoryToUpdate =
        repository
            .findById(id)
            .orElseThrow(
                () ->
                    new CategoryNotFoundException(
                        "Cannot update. Category with ID %d not found.".formatted(id)));

    log.debug("Found category to update. Current state: {}", categoryToUpdate);

    categoryToUpdate.setName(request.name());
    categoryToUpdate.setDescription(request.description());

    Category updatedCategory = repository.save(categoryToUpdate);
    log.info("Category with ID {} updated successfully.", updatedCategory.getId());

    return mapper.toCategoryResponse(updatedCategory);
  }

  /**
   * Deletes a category by its ID.
   *
   * @param id The ID of the category to delete.
   * @throws CategoryNotFoundException if the category with the given ID does not exist.
   */
  @Transactional
  @Caching(evict = {
          @CacheEvict(value = GET_CATEGORY_BY_ID, key = "#id"),
          @CacheEvict(value = GET_ALL_CATEGORIES, key = "'allCategories'")
  })
  public void deleteCategory(int id) {
    log.info("Attempting to delete category with ID: {}", id);
    if (!repository.existsById(id)) {
      throw new CategoryNotFoundException(
          "Cannot delete. Category with ID %d not found.".formatted(id));
    }
    repository.deleteById(id);
    log.info("Category with ID {} deleted successfully.", id);
  }
}
