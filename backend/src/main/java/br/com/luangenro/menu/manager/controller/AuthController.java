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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller implementation for the Authentication API.
 *
 * <p>Handles the logic for user login and registration, delegating tasks to the appropriate
 * services and managers.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController implements AuthApi {

  private final AuthenticationManager authenticationManager;
  private final UserDetailsService userDetailsService;
  private final JwtService jwtService;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<AuthResponse> login(AuthRequest request) {
    log.info("Authentication attempt for user: {}", request.username());
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.username(), request.password()));
    final UserDetails user = userDetailsService.loadUserByUsername(request.username());
    final String token = jwtService.generateToken(user);
    log.info("User '{}' authenticated successfully. Token generated.", user.getUsername());
    return ResponseEntity.ok(new AuthResponse(token));
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<UserResponse> registerUser(UserRequest userRequest) {
    log.info("Registration attempt for new user: {}", userRequest.username());
    // TODO: Add logic to check if username already exists to return a 400 Bad Request.
    User user =
        User.builder()
            .username(userRequest.username())
            .password(passwordEncoder.encode(userRequest.password()))
            .role(userRequest.role() != null ? userRequest.role() : Role.USER)
            .build();

    User savedUser = userRepository.save(user);

    log.info(
        "User '{}' registered successfully with ID {}.",
        savedUser.getUsername(),
        savedUser.getId());
    UserResponse response =
        new UserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getRole());
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
