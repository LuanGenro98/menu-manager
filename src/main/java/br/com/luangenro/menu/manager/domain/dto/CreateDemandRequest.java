package br.com.luangenro.menu.manager.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateDemandRequest(@NotEmpty List<Integer> itemsIds, @NotNull int tableNumber) {}
