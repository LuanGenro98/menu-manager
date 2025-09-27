package br.com.luangenro.menu.manager.controller;

import br.com.luangenro.menu.manager.domain.dto.CreateItemRequest;
import br.com.luangenro.menu.manager.domain.dto.CreateItemResponse;
import br.com.luangenro.menu.manager.domain.dto.ItemResponse;
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
 * <p>Handles HTTP requests and delegates the business logic to the {@link ItemService}.
 */
@RestController
@RequiredArgsConstructor
public class ItemController implements ItemApi {

  private final ItemService itemService;

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<ItemResponse> getItem(int id) {
    // TODO: Implement the logic to fetch a single item by its ID from the service.
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<List<ItemResponse>> getItems(Integer categoryId) {
    // TODO: Implement the logic to fetch items, applying the categoryId filter if it's not null.
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<CreateItemResponse> createItem(CreateItemRequest request) {
    var response = itemService.createItem(request);

    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(response.id())
            .toUri();

    return ResponseEntity.created(location).body(response);
  }
}
