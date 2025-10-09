package br.com.luangenro.menu.manager.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuration class for Swagger (OpenAPI 3.0) documentation.
 *
 * <p>This class customizes the OpenAPI specification to include API information and configure
 * security schemes for JWT Bearer Authentication.
 */
@Configuration
@Profile("!no-auth")
public class OpenApiConfig {

  /**
   * Creates a custom {@link OpenAPI} bean to configure the generated documentation.
   *
   * <p>This configuration defines a "Bearer Auth" security scheme for JWT and applies it globally.
   * This enables an "Authorize" button in the Swagger UI, allowing users to authenticate by
   * providing a JWT token to test protected endpoints.
   *
   * @return The customized {@code OpenAPI} object.
   */
  @Bean
  public OpenAPI customOpenAPI() {
    final String securitySchemeName = "bearerAuth";

    return new OpenAPI()
        .info(
            new Info()
                .title("Menu Manager API")
                .version("v1.0")
                .description("API for managing restaurant menus, categories, items, and demands."))
        // Add global security requirement
        .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
        // Define the security scheme component
        .components(
            new Components()
                .addSecuritySchemes(
                    securitySchemeName,
                    new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Enter JWT token obtained from the /login endpoint.")));
  }
}
