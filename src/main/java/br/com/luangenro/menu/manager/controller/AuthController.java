package br.com.luangenro.menu.manager.controller;

import br.com.luangenro.menu.manager.domain.dto.AuthRequest;
import br.com.luangenro.menu.manager.domain.dto.AuthResponse;
import br.com.luangenro.menu.manager.domain.dto.UserRequest;
import br.com.luangenro.menu.manager.domain.dto.UserResponse;
import br.com.luangenro.menu.manager.domain.entity.User;
import br.com.luangenro.menu.manager.domain.enumeration.Role;
import br.com.luangenro.menu.manager.repository.UserRepository;
import br.com.luangenro.menu.manager.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Controller dedicated to handling user authentication. */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final UserDetailsService userDetailsService;
  private final JwtService jwtService;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   * Authenticates a user and returns a JWT token upon success.
   *
   * @param request The authentication request containing username and password.
   * @return A {@link ResponseEntity} with the JWT token.
   */
  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.username(), request.password()));
    final UserDetails user = userDetailsService.loadUserByUsername(request.username());
    final String token = jwtService.generateToken(user);
    return ResponseEntity.ok(new AuthResponse(token));
  }

  /**
   * Public endpoint for registering a new user in the system. The user's password will be securely
   * hashed before being stored.
   *
   * @param userRequest The request body containing the new user's credentials and role.
   * @return A {@link ResponseEntity} with the details of the created user and a 201 Created status.
   */
  @PostMapping("/register")
  public ResponseEntity<UserResponse> registerUser(@RequestBody UserRequest userRequest) {
    User user =
        User.builder()
            .username(userRequest.username())
            .password(passwordEncoder.encode(userRequest.password()))
            .role(userRequest.role() != null ? userRequest.role() : Role.USER)
            .build();

    User savedUser = userRepository.save(user);

    UserResponse response =
        new UserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getRole());
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
