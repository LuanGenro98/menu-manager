package br.com.luangenro.menu.manager.controller;

import br.com.luangenro.menu.manager.domain.dto.AuthRequest;
import br.com.luangenro.menu.manager.domain.dto.AuthResponse;
import br.com.luangenro.menu.manager.domain.dto.UserRequest;
import br.com.luangenro.menu.manager.domain.dto.UserResponse;
import br.com.luangenro.menu.manager.exception.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Defines the public contract for user authentication and registration.
 *
 * <p>This interface centralizes all API definitions for auth-related operations, which are
 * considered public and do not require a JWT token for access.
 */
@Tag(name = "Authentication", description = "Endpoints for user login and registration")
@RequestMapping("/api/v1/auth")
public interface AuthApi {

  /**
   * Authenticates a user based on their credentials and returns a JWT token if successful.
   *
   * @param request The authentication request containing the username and password.
   * @return A {@link ResponseEntity} containing the {@link AuthResponse} with the JWT token.
   */
  @Operation(
      summary = "Authenticate a user",
      description = "Provides a JWT token for valid user credentials.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Authentication successful",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = AuthResponse.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Invalid request body format",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "403",
        description = "Authentication failed: Invalid credentials",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  @PostMapping("/login")
  ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest request);

  /**
   * Registers a new user in the system. The user's password will be securely hashed before being
   * stored.
   *
   * @param userRequest The request body containing the new user's details.
   * @return A {@link ResponseEntity} with the public details of the created user and a 201 Created
   *     status.
   */
  @Operation(
      summary = "Register a new user",
      description = "Public endpoint to create a new user account.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "201",
        description = "User registered successfully",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = UserResponse.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Invalid request data, such as a username that already exists",
        content =
            @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  @PostMapping("/register")
  ResponseEntity<UserResponse> registerUser(@RequestBody @Valid UserRequest userRequest);
}
