package br.com.luangenro.menu.manager.service;

import br.com.luangenro.menu.manager.domain.dto.*;
import br.com.luangenro.menu.manager.domain.entity.Category;
import br.com.luangenro.menu.manager.exception.CategoryNotFoundException;
import br.com.luangenro.menu.manager.mapper.CategoryMapper;
import br.com.luangenro.menu.manager.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Service layer responsible for business logic related to categories. */
@Service
@RequiredArgsConstructor
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
  public CategoryResponse getCategory(int id) {
    Category category =
        repository
            .findById(id)
            .orElseThrow(
                () ->
                    new CategoryNotFoundException("Category with ID %d not found.".formatted(id)));
    return mapper.toCategoryResponse(category);
  }

  /**
   * Retrieves a list of all categories.
   *
   * @return A list of {@link CategoryResponse}. Returns an empty list if no categories are found.
   */
  public List<CategoryResponse> getCategories() {
    return repository.findAll().stream().map(mapper::toCategoryResponse).toList();
  }

  /**
   * Creates a new category based on the provided request data.
   *
   * @param request The DTO containing the data for the new category.
   * @return A {@link CreateCategoryResponse} with the ID and name of the newly created category.
   */
  @Transactional
  public CreateCategoryResponse createCategory(CreateCategoryRequest request) {
    var category =
        Category.builder()
            .uuid(UUID.randomUUID())
            .name(request.name())
            .description(request.description())
            .build();

    var savedCategory = repository.save(category);

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
  public CategoryResponse updateCategory(int id, UpdateCategoryRequest request) {
    Category categoryToUpdate =
        repository
            .findById(id)
            .orElseThrow(
                () ->
                    new CategoryNotFoundException(
                        "Cannot update. Category with ID %d not found.".formatted(id)));

    categoryToUpdate.setName(request.name());
    categoryToUpdate.setDescription(request.description());

    Category updatedCategory = repository.save(categoryToUpdate);

    return mapper.toCategoryResponse(updatedCategory);
  }

  /**
   * Deletes a category by its ID.
   *
   * @param id The ID of the category to delete.
   * @throws CategoryNotFoundException if the category with the given ID does not exist.
   */
  @Transactional
  public void deleteCategory(int id) {
    if (!repository.existsById(id)) {
      throw new CategoryNotFoundException(
          "Cannot delete. Category with ID %d not found.".formatted(id));
    }
    repository.deleteById(id);
  }
}
