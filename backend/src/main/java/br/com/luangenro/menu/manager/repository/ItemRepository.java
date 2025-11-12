package br.com.luangenro.menu.manager.repository;

import br.com.luangenro.menu.manager.domain.entity.Item;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Spring Data JPA repository for the {@link Item} entity. */
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

  /**
   * Finds all items belonging to a specific category.
   *
   * @param categoryId The ID of the category.
   * @return A list of items for the given category.
   */
  List<Item> findByCategoryId(int categoryId);
}
