package br.com.luangenro.menu.manager.controller;

import br.com.luangenro.menu.manager.domain.dto.CreateItemRequest;
import br.com.luangenro.menu.manager.domain.dto.CreateItemResponse;
import br.com.luangenro.menu.manager.domain.dto.ItemResponse;
import br.com.luangenro.menu.manager.service.ItemService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("items")
@RequiredArgsConstructor
public class ItemController {

  private final ItemService itemService;

  @Value("${menumanager.endpoints.items}")
  private String ITEMS_ENDPOINT;

  ResponseEntity<ItemResponse> getItem() {
    return null;
  }

  ResponseEntity<List<ItemResponse>> getItemsFromCategory() {
    return null;
  }

  @PostMapping("create")
  ResponseEntity<CreateItemResponse> createItem(@RequestBody @Valid CreateItemRequest request) {
    var response = itemService.createItem(request);
    return ResponseEntity.created(URI.create(ITEMS_ENDPOINT + response.id())).body(response);
  }
}
