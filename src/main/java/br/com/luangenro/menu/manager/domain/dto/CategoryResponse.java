package br.com.luangenro.menu.manager.domain.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record CategoryResponse(int id, String name, String description, List<ItemResponse> items) {}
