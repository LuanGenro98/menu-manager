package br.com.luangenro.menu.manager.service;

import br.com.luangenro.menu.manager.domain.entity.User;
import br.com.luangenro.menu.manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  /** Obtém o usuário autenticado no contexto de segurança atual (via Token JWT). */
  public User getCurrentUser() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new IllegalStateException("Nenhum usuário autenticado encontrado.");
    }

    Object principal = authentication.getPrincipal();
    String username;

    if (principal instanceof UserDetails userDetails) {
      username = userDetails.getUsername();
    } else {
      username = principal.toString();
    }

    return userRepository
        .findByUsername(username)
        .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado no banco."));
  }
}
