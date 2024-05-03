package br.com.luangenro.menu.manager.domain.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CategoryResponse(int id,
                               String name,
                               String description,
                               List<ItemResponse> items) {
}
