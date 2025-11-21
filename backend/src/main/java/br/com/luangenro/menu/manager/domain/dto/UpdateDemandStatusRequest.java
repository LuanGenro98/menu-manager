package br.com.luangenro.menu.manager.domain.dto;

import br.com.luangenro.menu.manager.domain.enumeration.DemandStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/** Represents the request to update the status of a demand. */
@Schema(description = "Payload to update the status of an existing demand.")
public record UpdateDemandStatusRequest(
    @Schema(
            description = "The new status for the demand.",
            example = "PREPARING",
            requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        DemandStatus newStatus) {}
