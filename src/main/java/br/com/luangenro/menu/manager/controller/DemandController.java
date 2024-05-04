package br.com.luangenro.menu.manager.controller;

import br.com.luangenro.menu.manager.domain.dto.CreateDemandRequest;
import br.com.luangenro.menu.manager.domain.dto.CreateDemandResponse;
import br.com.luangenro.menu.manager.service.DemandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("demands")
@RequiredArgsConstructor
public class DemandController {

    private final DemandService service;

    @Value("${menumanager.endpoints.demands}")
    private String DEMANDS_ENDPOINT;

    @PostMapping("create")
    ResponseEntity<CreateDemandResponse> createDemand(@RequestBody @Valid CreateDemandRequest createDemandRequest) {
        var createDemandResponse = service.createDemand(createDemandRequest);
        return ResponseEntity.created(URI.create(DEMANDS_ENDPOINT + "/" + createDemandResponse.id()))
                .body(createDemandResponse);
    }
}
