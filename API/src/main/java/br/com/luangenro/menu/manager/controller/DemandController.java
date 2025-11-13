package br.com.luangenro.menu.manager.controller;

import br.com.luangenro.menu.manager.domain.dto.CreateDemandRequest;
import br.com.luangenro.menu.manager.domain.dto.CreateDemandResponse;
import br.com.luangenro.menu.manager.service.DemandService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
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
public class DemandController implements DemandApi {

  private final DemandService service;

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<CreateDemandResponse> createDemand(
      CreateDemandRequest createDemandRequest) {
    var createDemandResponse = service.createDemand(createDemandRequest);

    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createDemandResponse.id())
            .toUri();

    return ResponseEntity.created(location).body(createDemandResponse);
  }
}
