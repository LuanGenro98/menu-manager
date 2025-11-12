package br.com.luangenro.menu.manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configures Cross-Origin Resource Sharing (CORS) for the application. This is necessary to allow
 * the frontend application to communicate with the backend API.
 */
@Configuration
public class CorsConfig {

  /**
   * Defines the global CORS configuration.
   *
   * @return a WebMvcConfigurer with the CORS mappings.
   */
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        // Allows all endpoints under /api/ to be called from the specified origin.
        registry
            .addMapping("/api/**")
            // IMPORTANTE: Altere para a URL do seu frontend em produção
            .allowedOrigins("http://localhost:3000")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true);
      }
    };
  }
}
