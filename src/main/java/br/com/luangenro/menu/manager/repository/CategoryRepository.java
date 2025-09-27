package br.com.luangenro.menu.manager.repository;

import br.com.luangenro.menu.manager.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {}
