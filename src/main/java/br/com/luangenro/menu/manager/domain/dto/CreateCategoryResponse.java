package br.com.luangenro.menu.manager.domain.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateCategoryResponse(UUID uuid,
                                     String name) {
}
