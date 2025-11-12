package br.com.luangenro.menu.manager.controller;

import br.com.luangenro.menu.manager.domain.dto.CreateDemandRequest;
import br.com.luangenro.menu.manager.domain.dto.CreateDemandResponse;
import br.com.luangenro.menu.manager.domain.dto.DemandResponse;
import br.com.luangenro.menu.manager.domain.dto.UpdateDemandStatusRequest;
import br.com.luangenro.menu.manager.domain.enumeration.DemandStatus;
import br.com.luangenro.menu.manager.service.DemandService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Controller implementation for the Demand API.
 *
 * <p>Handles HTTP requests and delegates the business logic to the {@link DemandService}.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class DemandController implements DemandApi {

  private final DemandService service;

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<CreateDemandResponse> createDemand(
      CreateDemandRequest createDemandRequest) {
    log.info(
        "Received request to create new demand for table {} with {} items.",
        createDemandRequest.tableNumber(),
        createDemandRequest.itemsIds().size());
    var createDemandResponse = service.createDemand(createDemandRequest);

    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createDemandResponse.id())
            .toUri();

    log.info(
        "Demand created with ID {}. Responding with 201 Created at location: {}",
        createDemandResponse.id(),
        location);

    return ResponseEntity.created(location).body(createDemandResponse);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<DemandResponse> getDemand(int id) {
    log.info("Received request to get demand with ID: {}", id);
    return ResponseEntity.ok(service.getDemand(id));
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<List<DemandResponse>> getAllDemands(DemandStatus status) {
    log.info("Received request to get all demands with status filter: {}", status);
    return ResponseEntity.ok(service.getAllDemands(status));
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<DemandResponse> updateDemandStatus(
      int id, UpdateDemandStatusRequest request) {
    log.info("Received request to update status of demand ID {} to '{}'.", id, request.newStatus());
    return ResponseEntity.ok(service.updateDemandStatus(id, request));
  }
}
