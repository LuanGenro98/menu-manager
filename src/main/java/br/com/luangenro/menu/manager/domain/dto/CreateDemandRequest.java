package br.com.luangenro.menu.manager.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * Represents the data required to create a new demand.
 *
 * @param itemsIds A list of unique identifiers for the menu items being ordered. Cannot be empty.
 * @param tableNumber The number of the table placing the demand. Cannot be null.
 */
@Schema(description = "Payload for creating a new customer demand.")
public record CreateDemandRequest(
    @Schema(
            description = "A list of IDs for the items being ordered.",
            example = "[101, 105, 203]",
            requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty
        List<Integer> itemsIds,
    @Schema(
            description = "The number of the table placing the demand.",
            example = "12",
            requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        Integer tableNumber) {}
