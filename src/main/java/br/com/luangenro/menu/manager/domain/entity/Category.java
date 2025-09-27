package br.com.luangenro.menu.manager.domain.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Represents the Category entity in the database. Each category can contain multiple menu items.
 */
@Entity
@Table(name = "category", schema = "public")
@Getter
@Setter
@ToString(exclude = "items") // Avoids potential infinite loops in logs
@EqualsAndHashCode(of = "id") // Uses only the ID for equality checks
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {

  /** The unique identifier for the category (primary key). */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  /** A universally unique identifier (UUID) for external references. */
  @Column(name = "uuid", nullable = false, unique = true)
  private UUID uuid;

  /** The name of the category (e.g., "Beverages"). */
  @Column(name = "name", nullable = false, length = 50)
  private String name;

  /** A short description of the category. */
  @Column(name = "description", nullable = false, length = 100)
  private String description;

  /**
   * The list of items belonging to this category. Mapped by the 'category' field in the Item
   * entity.
   */
  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Item> items = new ArrayList<>();
}
