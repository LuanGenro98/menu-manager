package br.com.luangenro.menu.manager.domain.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ItemResponse (UUID uuid,
                           String name,
                           String description,
                           Double price){
}
