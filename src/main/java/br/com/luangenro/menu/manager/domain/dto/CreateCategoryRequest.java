package br.com.luangenro.menu.manager.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequest(@NotBlank @Size(max = 50) String name,
                                    @NotBlank @Size(max = 100) String description) {
}
