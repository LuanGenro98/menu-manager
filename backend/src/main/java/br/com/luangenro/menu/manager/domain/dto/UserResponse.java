package br.com.luangenro.menu.manager.domain.dto;

import br.com.luangenro.menu.manager.domain.enumeration.Role;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Represents the data sent back to the client after a user is successfully created. Excludes
 * sensitive information like the password.
 *
 * @param id The unique system-generated identifier for the user.
 * @param username The username of the created account.
 * @param role The role assigned to the user.
 */
@Schema(description = "Public data of a user account after creation.")
public record UserResponse(
    @Schema(description = "The unique system-generated identifier for the user.", example = "10")
        Integer id,
    @Schema(description = "The username of the created account.", example = "newuser")
        String username,
    @Schema(description = "The role assigned to the user.", example = "USER") Role role) {}
