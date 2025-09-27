package br.com.luangenro.menu.manager.controller;

import br.com.luangenro.menu.manager.domain.dto.CreateDemandRequest;
import br.com.luangenro.menu.manager.domain.dto.CreateDemandResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Defines the public contract for managing customer demands (orders).
 *
 * <p>This interface centralizes all API definitions for demand-related operations.
 */
@Tag(name = "Demands", description = "Endpoints for creating and managing customer demands")
@RequestMapping("/api/v1/demands")
public interface DemandApi {

  /**
   * Creates a new demand for a specific table with a list of items.
   *
   * @param createDemandRequest The request body containing the list of item IDs and the table
   *     number.
   * @return A {@link ResponseEntity} with the {@link CreateDemandResponse} of the new demand, a
   *     location header, and an HTTP 201 (Created) status.
   */
  @Operation(summary = "Create a new demand")
  @ApiResponses({
    @ApiResponse(
        responseCode = "201",
        description = "Demand created successfully",
        content = {
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = CreateDemandResponse.class))
        }),
    @ApiResponse(
        responseCode = "400",
        description = "Invalid request data, such as an empty item list or non-existent item IDs",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  @PostMapping
  ResponseEntity<CreateDemandResponse> createDemand(
      @RequestBody @Valid CreateDemandRequest createDemandRequest);
}
