package br.com.luangenro.menu.manager.repository;

import br.com.luangenro.menu.manager.domain.entity.Demand;
import br.com.luangenro.menu.manager.domain.enumeration.DemandStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandRepository extends JpaRepository<Demand, Integer> {
  /**
   * Finds all demands that match a specific status.
   *
   * <p>This method allows for filtering the demands based on their current lifecycle state, such as
   * ORDERED, PREPARING, or COMPLETED.
   *
   * @param status The {@link DemandStatus} to filter by.
   * @return A list of {@link Demand} entities matching the given status. The list will be empty if
   *     no demands are found with that status.
   */
  List<Demand> findByStatus(DemandStatus status);

  /**
   * Finds all demands and eagerly fetches their associated demand items in a single query. This
   * avoids the N+1 problem.
   *
   * @return A list of {@link Demand} entities with their items initialized.
   */
  @Query("SELECT d FROM Demand d LEFT JOIN FETCH d.demandItems")
  List<Demand> findAllWithItems();

  /**
   * Finds a single demand by its ID and eagerly fetches its associated demand items.
   *
   * @param id The ID of the demand.
   * @return An Optional containing the {@link Demand} if found.
   */
  @Query(
      "SELECT d FROM Demand d LEFT JOIN FETCH d.demandItems di LEFT JOIN FETCH di.item WHERE d.id = :id")
  Optional<Demand> findByIdWithItems(@Param("id") int id);
}
