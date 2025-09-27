package br.com.luangenro.menu.manager.domain.dto;

import lombok.Builder;

@Builder
public record CreateCategoryResponse(int id, String name) {}
