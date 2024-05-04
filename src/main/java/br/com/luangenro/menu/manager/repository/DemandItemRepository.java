package br.com.luangenro.menu.manager.repository;

import br.com.luangenro.menu.manager.domain.entity.DemandItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandItemRepository extends JpaRepository<DemandItem, Integer> {
}
