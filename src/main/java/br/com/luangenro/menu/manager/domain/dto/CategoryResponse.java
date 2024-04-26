package br.com.luangenro.menu.manager.domain.dto;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record CategoryResponse(UUID uuid,
                               String name,
                               String description,
                               List<ItemResponse> items) {
}
