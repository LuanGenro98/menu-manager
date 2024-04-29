package br.com.luangenro.menu.manager.domain.dto;

import lombok.Builder;

@Builder
public record ItemResponse(Integer id,
                           String name,
                           String description,
                           Double price) {
}
