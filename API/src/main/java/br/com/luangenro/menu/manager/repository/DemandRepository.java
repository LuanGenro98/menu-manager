package br.com.luangenro.menu.manager.repository;

import br.com.luangenro.menu.manager.domain.entity.Demand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandRepository extends JpaRepository<Demand, Integer> {}
