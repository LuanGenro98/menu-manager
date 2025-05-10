package br.com.luangenro.menu.manager.service;

import br.com.luangenro.menu.manager.domain.dto.CreateItemRequest;
import br.com.luangenro.menu.manager.domain.dto.CreateItemResponse;
import br.com.luangenro.menu.manager.domain.entity.Category;
import br.com.luangenro.menu.manager.domain.entity.Item;
import br.com.luangenro.menu.manager.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemService {

  private final ItemRepository repository;

  public CreateItemResponse createItem(CreateItemRequest request) {
    var item = Item
        .builder()
        .uuid(UUID.randomUUID())
        .name(request.name())
        .description(request.description())
        .price(request.price())
        .category(new Category(request.categoryId()))
        .build();
    repository.save(item);
    return new CreateItemResponse(item.getId(), item.getName());
  }
}
