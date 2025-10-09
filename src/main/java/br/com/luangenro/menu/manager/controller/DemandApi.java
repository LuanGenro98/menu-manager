package br.com.luangenro.menu.manager.controller;

import br.com.luangenro.menu.manager.domain.dto.CreateDemandRequest;
import br.com.luangenro.menu.manager.domain.dto.CreateDemandResponse;
import br.com.luangenro.menu.manager.domain.dto.DemandResponse;
import br.com.luangenro.menu.manager.domain.dto.UpdateDemandStatusRequest;
import br.com.luangenro.menu.manager.domain.enumeration.DemandStatus;
import br.com.luangenro.menu.manager.exception.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  /**
   * Retrieves a single demand by its ID.
   *
   * @param id The unique identifier of the demand.
   * @return A {@link ResponseEntity} with the demand details.
   */
  @Operation(summary = "Find a demand by its ID")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Demand found"),
    @ApiResponse(responseCode = "404", description = "Demand not found", content = @Content)
  })
  @GetMapping("/{id}")
  ResponseEntity<DemandResponse> getDemand(@PathVariable int id);

  /**
   * Retrieves a list of all demands, optionally filtered by status.
   *
   * @param status (Optional) Filter demands by a specific status.
   * @return A {@link ResponseEntity} with a list of demands.
   */
  @Operation(
      summary = "List all demands",
      description = "Lists all demands, can be filtered by status (e.g., ORDERED, PREPARING).")
  @ApiResponse(responseCode = "200", description = "A list of demands")
  @GetMapping
  ResponseEntity<List<DemandResponse>> getAllDemands(
      @RequestParam(required = false) DemandStatus status);

  /**
   * Updates the status of an existing demand.
   *
   * @param id The ID of the demand to update.
   * @param request The request containing the new status.
   * @return A {@link ResponseEntity} with the updated demand.
   */
  @Operation(
      summary = "Update a demand's status",
      description = "Protected endpoint for staff to update the order status.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Status updated successfully"),
    @ApiResponse(responseCode = "404", description = "Demand not found", content = @Content)
  })
  @PatchMapping("/{id}/status")
  ResponseEntity<DemandResponse> updateDemandStatus(
      @PathVariable int id, @RequestBody @Valid UpdateDemandStatusRequest request);
}
