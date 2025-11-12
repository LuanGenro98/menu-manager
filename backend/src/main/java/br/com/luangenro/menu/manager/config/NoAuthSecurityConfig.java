package br.com.luangenro.menu.manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration specifically for the 'no-auth' profile.
 *
 * <p>When the 'no-auth' profile is active, this configuration is applied, effectively disabling all
 * security checks and permitting all requests. This is intended for development, testing, or
 * evaluation purposes only.
 */
@Configuration
@EnableWebSecurity
@Profile("no-auth")
public class NoAuthSecurityConfig {

  /** A security filter chain that permits all HTTP requests without authentication. */
  @Bean
  public SecurityFilterChain noAuthFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
    return http.build();
  }
}
