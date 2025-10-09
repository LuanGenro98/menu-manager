package br.com.luangenro.menu.manager.domain.dto;

import br.com.luangenro.menu.manager.domain.enumeration.DemandStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/** Represents the full details of a demand. */
@Schema(description = "Represents the full details of a customer demand (order).")
public record DemandResponse(
    @Schema(description = "Unique ID of the demand.", example = "54321") int id,
    @Schema(description = "The table number for which the demand was created.", example = "12")
        int tableNumber,
    @Schema(description = "Current status of the demand.", example = "PREPARING")
        DemandStatus status,
    @Schema(description = "Total price of the demand.", example = "75.50") BigDecimal price,
    @Schema(description = "Timestamp of when the demand was created.") Instant createdAt,
    @Schema(description = "List of items included in the demand.")
        List<DemandItemResponse> items) {}
