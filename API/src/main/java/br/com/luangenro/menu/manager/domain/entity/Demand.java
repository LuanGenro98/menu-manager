package br.com.luangenro.menu.manager.domain.entity;

import br.com.luangenro.menu.manager.domain.enumeration.DemandStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "demand", schema = "public")
@Data
@Builder
public class Demand {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "uuid")
  private UUID uuid;

  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private DemandStatus status;

  @Column(name = "price")
  private BigDecimal price;

  @Column(name = "table_number")
  private int tableNumber;

  @OneToMany(mappedBy = "demand")
  private List<DemandItem> demandItems;
}
