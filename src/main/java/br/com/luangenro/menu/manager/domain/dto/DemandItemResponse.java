package br.com.luangenro.menu.manager.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/** Represents a single item within a demand response. */
@Schema(description = "Represents a single item within a returned demand.")
public record DemandItemResponse(
    @Schema(description = "Name of the item.", example = "Cheeseburger") String name,
    @Schema(description = "Price of the item at the time of purchase.", example = "25.50")
        BigDecimal price) {}
