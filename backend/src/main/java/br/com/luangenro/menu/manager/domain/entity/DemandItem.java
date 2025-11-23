package br.com.luangenro.menu.manager.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "demand_item", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DemandItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "demand_id", nullable = false)
  private Demand demand;

  @ManyToOne
  @JoinColumn(name = "item_id")
  private Item item;
}
