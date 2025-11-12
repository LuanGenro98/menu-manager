package br.com.luangenro.menu.manager.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Represents the response after a successful authentication.
 *
 * @param token The generated JWT token for the user.
 */
@Schema(description = "Payload containing the JWT token after a successful login.")
public record AuthResponse(
    @Schema(
            description = "The generated JWT token for the user.",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbi...")
        String token) {}
