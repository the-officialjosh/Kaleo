package dev.joshuaonyema.kaleo.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;

/**
 * Configures a custom JwtDecoder that can handle the Docker networking issuer mismatch.
 * The browser fetches tokens from localhost:9095, but the container validates using keycloak:8080.
 * We fetch keys from the container-accessible URL but accept tokens with the browser issuer.
 */
@Configuration
public class JwtDecoderConfig {

    @Value("${keycloak.jwk-set-uri:http://keycloak:8080/realms/kaleo-events/protocol/openid-connect/certs}")
    private String jwkSetUri;

    @Value("${keycloak.browser-url:http://localhost:9095/realms/kaleo-events}")
    private String browserIssuerUrl;

    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
        
        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(
                new JwtTimestampValidator(),
                new JwtIssuerValidator(browserIssuerUrl)
        );
        
        decoder.setJwtValidator(validator);
        return decoder;
    }
}
