package br.com.luangenro.menu.manager.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Represents the response after a new demand has been successfully created.
 *
 * @param id The unique, system-generated identifier for the new demand.
 * @param tableNumber The table number associated with the created demand.
 */
@Schema(description = "Response payload after successfully creating a demand.")
public record CreateDemandResponse(
    @Schema(description = "The unique ID of the created demand.", example = "54321") Integer id,
    @Schema(description = "The table number for which the demand was created.", example = "12")
        Integer tableNumber) {}
