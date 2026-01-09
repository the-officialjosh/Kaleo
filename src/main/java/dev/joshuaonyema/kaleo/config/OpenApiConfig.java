package dev.joshuaonyema.kaleo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI / Swagger UI configuration with OAuth2 Keycloak integration.
 * Enables the "Authorize" button in Swagger UI to authenticate via Keycloak.
 * 
 * IMPORTANT: Uses browser-accessible Keycloak URL, NOT container-internal URL.
 * The browser must be able to reach these endpoints.
 */
@Configuration
public class OpenApiConfig {

    // Browser-accessible Keycloak URL (e.g., http://localhost:9095/realms/kaleo-events)
    // NOT the container URL (http://keycloak:8080/...)
    @Value("${keycloak.browser-url}")
    private String keycloakBrowserUrl;

    @Bean
    public OpenAPI kaleoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Kaleo API")
                        .description("REST API for church and ministry program management. " +
                                "Supports event creation, pass management, and QR code validation.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Joshua Onyema")
                                .url("https://github.com/the-officialjosh/Kaleo"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .addSecurityItem(new SecurityRequirement().addList("oauth2"))
                .components(new Components()
                        .addSecuritySchemes("oauth2", new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .description("OAuth2 Authorization Code flow with Keycloak. " +
                                        "Click Authorize, then login with your Keycloak credentials.")
                                .flows(new OAuthFlows()
                                        .authorizationCode(new OAuthFlow()
                                                .authorizationUrl(keycloakBrowserUrl + "/protocol/openid-connect/auth")
                                                .tokenUrl(keycloakBrowserUrl + "/protocol/openid-connect/token")))));
    }
}
