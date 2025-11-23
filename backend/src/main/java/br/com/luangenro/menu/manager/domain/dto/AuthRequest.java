package br.com.luangenro.menu.manager.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Represents the request body for an authentication attempt.
 *
 * @param username The user's username.
 * @param password The user's plain text password.
 */
@Schema(description = "Credentials required for user authentication.")
public record AuthRequest(
    @Schema(
            description = "The user's username.",
            example = "luan",
            requiredMode = Schema.RequiredMode.REQUIRED)
        String username,
    @Schema(
            description = "The user's plain text password.",
            example = "123",
            requiredMode = Schema.RequiredMode.REQUIRED)
        String password) {}
