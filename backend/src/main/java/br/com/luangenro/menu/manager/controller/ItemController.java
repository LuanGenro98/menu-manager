package br.com.luangenro.menu.manager.controller;

import br.com.luangenro.menu.manager.domain.dto.CreateItemRequest;
import br.com.luangenro.menu.manager.domain.dto.CreateItemResponse;
import br.com.luangenro.menu.manager.domain.dto.ItemResponse;
import br.com.luangenro.menu.manager.domain.dto.UpdateItemRequest;
import br.com.luangenro.menu.manager.service.ImageService;
import br.com.luangenro.menu.manager.service.ItemService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Controller implementation for the Item API. Handles HTTP requests and delegates the business
 * logic to the {@link ItemService}.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class ItemController implements ItemApi {

  private final ItemService itemService;
  private final ImageService imageService;

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<ItemResponse> getItem(int id) {
    log.info("Received request to get item with ID: {}", id);
    ItemResponse response = itemService.getItem(id);
    log.info("Responding with item ID {} found.", id);
    return ResponseEntity.ok(response);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<List<ItemResponse>> getItems(Integer categoryId) {
    if (categoryId != null) {
      log.info("Received request to get all items for category ID: {}", categoryId);
    } else {
      log.info("Received request to get all items.");
    }
    List<ItemResponse> response = itemService.getItems(categoryId);
    log.info("Responding with a list of {} items.", response.size());
    return ResponseEntity.ok(response);
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation constructs the 'Location' header for the newly created resource using
   * Spring's {@code ServletUriComponentsBuilder}.
   */
  @Override
  public ResponseEntity<CreateItemResponse> createItemWithImage(
      CreateItemRequest request, MultipartFile image) {

    CreateItemResponse response = itemService.createItem(request, image);

    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(response.id())
            .toUri();

    return ResponseEntity.created(location).body(response);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<ItemResponse> updateItem(
      int id, UpdateItemRequest request, MultipartFile image) {
    log.info("Received request to update item with ID: {}", id);
    ItemResponse updatedItem = itemService.updateItem(id, request, image);
    log.info("Item with ID {} updated successfully. Responding with 200 OK.", updatedItem.id());
    return ResponseEntity.ok(updatedItem);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<Void> deleteItem(int id) {
    log.info("Received request to delete item with ID: {}", id);
    itemService.deleteItem(id);
    log.info("Item with ID {} deleted successfully. Responding with 204 No Content.", id);
    return ResponseEntity.noContent().build();
  }
}
