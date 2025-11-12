package br.com.luangenro.menu.manager.domain.dto;

import br.com.luangenro.menu.manager.domain.enumeration.Role;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Represents the data required to register a new user.
 *
 * @param username The desired username for the new account.
 * @param password The plain text password for the new account.
 * @param role The role to be assigned to the new user.
 */
@Schema(description = "Data required to register a new user.")
public record UserRequest(
    @Schema(
            description = "The desired username for the new account.",
            example = "newuser",
            requiredMode = Schema.RequiredMode.REQUIRED)
        String username,
    @Schema(
            description = "The plain text password for the new account.",
            example = "strongPassword!@#",
            requiredMode = Schema.RequiredMode.REQUIRED)
        String password,
    @Schema(
            description = "The role to be assigned to the new user.",
            example = "USER",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Role role) {}
