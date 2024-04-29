package br.com.luangenro.menu.manager.domain.dto;

import jakarta.validation.constraints.*;

public record CreateItemRequest(@NotBlank @Size(max = 50) String name,
                                @NotBlank @Size(max = 100) String description,
                                @NotNull @PositiveOrZero Double price,
                                @NotNull @Positive Integer categoryId) {
}