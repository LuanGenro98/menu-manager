package br.com.luangenro.menu.manager.service;

import br.com.luangenro.menu.manager.domain.dto.*;
import br.com.luangenro.menu.manager.domain.dto.CreateItemRequest;
import br.com.luangenro.menu.manager.domain.dto.CreateItemResponse;
import br.com.luangenro.menu.manager.domain.dto.ItemResponse;
import br.com.luangenro.menu.manager.domain.dto.UpdateItemRequest;
import br.com.luangenro.menu.manager.domain.entity.Category;
import br.com.luangenro.menu.manager.domain.entity.Item;
import br.com.luangenro.menu.manager.exception.CategoryNotFoundException;
import br.com.luangenro.menu.manager.exception.ItemNotFoundException;
import br.com.luangenro.menu.manager.mapper.ItemMapper;
import br.com.luangenro.menu.manager.repository.CategoryRepository;
import br.com.luangenro.menu.manager.repository.ItemRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Service layer responsible for business logic related to menu items. */
@Service
@RequiredArgsConstructor
public class ItemService {

  private final ItemRepository itemRepository;
  private final CategoryRepository categoryRepository;
  private final ItemMapper mapper;

  /**
   * Retrieves a single item by its ID.
   *
   * @param id The unique identifier of the item.
   * @return An {@link ItemResponse} containing the item details.
   * @throws ItemNotFoundException if no item with the given ID is found.
   */
  @Transactional
  public ItemResponse getItem(int id) {
    Item item =
        itemRepository
            .findById(id)
            .orElseThrow(
                () -> new ItemNotFoundException("Item with ID %d not found.".formatted(id)));
    return mapper.toItemResponse(item);
  }

  /**
   * Retrieves a list of all items, with an option to filter by category.
   *
   * @param categoryId An optional ID to filter items by a specific category. Can be null.
   * @return A list of {@link ItemResponse}. Returns an empty list if no items are found.
   */
  @Transactional
  public List<ItemResponse> getItems(Integer categoryId) {
    List<Item> items;
    if (categoryId != null) {
      items = itemRepository.findByCategoryId(categoryId);
    } else {
      items = itemRepository.findAll();
    }
    return items.stream().map(mapper::toItemResponse).toList();
  }

  /**
   * Creates a new item and associates it with an existing category.
   *
   * @param request The DTO containing the data for the new item.
   * @return A {@link CreateItemResponse} with the ID and name of the newly created item.
   * @throws CategoryNotFoundException if the category specified in the request does not exist.
   */
  @Transactional
  public CreateItemResponse createItem(CreateItemRequest request) {
    Category category =
        categoryRepository
            .findById(request.categoryId())
            .orElseThrow(
                () ->
                    new CategoryNotFoundException(
                        "Cannot create item. Category with ID %d not found."
                            .formatted(request.categoryId())));

    var item =
        Item.builder()
            .uuid(UUID.randomUUID())
            .name(request.name())
            .description(request.description())
            .price(request.price())
            .category(category)
            .build();

    var savedItem = itemRepository.save(item);
    return new CreateItemResponse(savedItem.getId(), savedItem.getName());
  }

  /**
   * Updates an existing item with new data.
   *
   * @param id The ID of the item to update.
   * @param request The DTO containing the new data for the item.
   * @return An {@link ItemResponse} representing the updated state of the item.
   * @throws ItemNotFoundException if the item with the given ID does not exist.
   * @throws CategoryNotFoundException if the new category specified in the request does not exist.
   */
  @Transactional
  public ItemResponse updateItem(int id, UpdateItemRequest request) {
    Item itemToUpdate =
        itemRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new ItemNotFoundException(
                        "Cannot update. Item with ID %d not found.".formatted(id)));

    Category category =
        categoryRepository
            .findById(request.categoryId())
            .orElseThrow(
                () ->
                    new CategoryNotFoundException(
                        "Cannot update item. Category with ID %d not found."
                            .formatted(request.categoryId())));

    itemToUpdate.setName(request.name());
    itemToUpdate.setDescription(request.description());
    itemToUpdate.setPrice(request.price());
    itemToUpdate.setCategory(category);

    Item updatedItem = itemRepository.save(itemToUpdate);
    return mapper.toItemResponse(updatedItem);
  }

  /**
   * Deletes an item by its ID.
   *
   * @param id The ID of the item to delete.
   * @throws ItemNotFoundException if the item with the given ID does not exist.
   */
  @Transactional
  public void deleteItem(int id) {
    if (!itemRepository.existsById(id)) {
      throw new ItemNotFoundException("Cannot delete. Item with ID %d not found.".formatted(id));
    }
    itemRepository.deleteById(id);
  }
}
