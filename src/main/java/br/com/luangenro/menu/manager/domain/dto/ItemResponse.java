package br.com.luangenro.menu.manager.domain.dto;

import lombok.Builder;

@Builder
public record ItemResponse(int id, String name, String description, Double price) {}
