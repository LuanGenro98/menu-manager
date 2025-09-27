package br.com.luangenro.menu.manager.controller;

import br.com.luangenro.menu.manager.domain.dto.CreateItemRequest;
import br.com.luangenro.menu.manager.domain.dto.CreateItemResponse;
import br.com.luangenro.menu.manager.domain.dto.ItemResponse;
import br.com.luangenro.menu.manager.domain.dto.UpdateItemRequest;
import br.com.luangenro.menu.manager.service.ItemService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Controller implementation for the Item API.
 *
 * <p>Handles HTTP requests and delegates the business logic to the {@link ItemService}. This class
 * implements the contract defined in {@link ItemApi}.
 */
@RestController
@RequiredArgsConstructor
public class ItemController implements ItemApi {

  private final ItemService itemService;

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<ItemResponse> getItem(int id) {
    return ResponseEntity.ok(itemService.getItem(id));
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<List<ItemResponse>> getItems(Integer categoryId) {
    return ResponseEntity.ok(itemService.getItems(categoryId));
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation constructs the 'Location' header for the newly created resource using
   * Spring's {@code ServletUriComponentsBuilder}.
   */
  @Override
  public ResponseEntity<CreateItemResponse> createItem(CreateItemRequest request) {
    CreateItemResponse response = itemService.createItem(request);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(response.id())
            .toUri();
    return ResponseEntity.created(location).body(response);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<ItemResponse> updateItem(int id, UpdateItemRequest request) {
    return ResponseEntity.ok(itemService.updateItem(id, request));
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<Void> deleteItem(int id) {
    itemService.deleteItem(id);
    return ResponseEntity.noContent().build();
  }
}
