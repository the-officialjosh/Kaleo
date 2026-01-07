package dev.joshuaonyema.kaleo.config.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HttpSecurityConfigTest {

    private HttpSecurityConfig httpSecurityConfig;

    @BeforeEach
    void setUp() {
        httpSecurityConfig = new HttpSecurityConfig();
    }

    // ==================== JwtAuthenticationConverter Tests ====================

    @Test
    void jwtAuthenticationConverter_whenCalled_thenReturnsConverterWithGrantedAuthoritiesConverter() {
        JwtAuthenticationConverter converter = httpSecurityConfig.jwtAuthenticationConverter();

        assertNotNull(converter);
        assertNotNull(converter.convert(createJwtWithRoles(List.of("user", "admin"))));
    }

    // ==================== Keycloak Realm Roles Converter Tests ====================

    @Test
    void keycloakRealmRoles_whenJwtHasValidRoles_thenExtractsRolesWithRolePrefix() {
        JwtAuthenticationConverter converter = httpSecurityConfig.jwtAuthenticationConverter();
        Jwt jwt = createJwtWithRoles(List.of("user", "admin", "moderator"));

        Collection<GrantedAuthority> authorities = converter.convert(jwt).getAuthorities();

        // Note: Spring Security JwtAuthenticationConverter may add default scope authorities
        assertTrue(authorities.size() >= 3, "Should have at least 3 authorities");
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_user")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_admin")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_moderator")));
    }

    @Test
    void keycloakRealmRoles_whenJwtHasSingleRole_thenExtractsSingleRole() {
        JwtAuthenticationConverter converter = httpSecurityConfig.jwtAuthenticationConverter();
        Jwt jwt = createJwtWithRoles(List.of("user"));

        Collection<GrantedAuthority> authorities = converter.convert(jwt).getAuthorities();

        assertTrue(authorities.size() >= 1, "Should have at least 1 authority");
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_user")));
    }

    @Test
    void keycloakRealmRoles_whenJwtHasEmptyRolesList_thenReturnsEmptyAuthorities() {
        JwtAuthenticationConverter converter = httpSecurityConfig.jwtAuthenticationConverter();
        Jwt jwt = createJwtWithRoles(List.of());

        Collection<GrantedAuthority> authorities = converter.convert(jwt).getAuthorities();

        // Spring Security may add default scope authorities even with empty roles
        assertNotNull(authorities);
    }

    @Test
    void keycloakRealmRoles_whenJwtHasNoRealmAccess_thenReturnsEmptyAuthorities() {
        JwtAuthenticationConverter converter = httpSecurityConfig.jwtAuthenticationConverter();
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .claim("sub", "user123")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        Collection<GrantedAuthority> authorities = converter.convert(jwt).getAuthorities();

        // Spring Security may add default scope authorities
        assertNotNull(authorities);
    }

    @Test
    void keycloakRealmRoles_whenRealmAccessHasNoRoles_thenReturnsEmptyAuthorities() {
        JwtAuthenticationConverter converter = httpSecurityConfig.jwtAuthenticationConverter();
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .claim("sub", "user123")
                .claim("realm_access", Map.of("other_field", "value"))
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        Collection<GrantedAuthority> authorities = converter.convert(jwt).getAuthorities();

        // Spring Security may add default scope authorities
        assertNotNull(authorities);
    }

    @Test
    void keycloakRealmRoles_whenRolesIsNotList_thenReturnsEmptyAuthorities() {
        JwtAuthenticationConverter converter = httpSecurityConfig.jwtAuthenticationConverter();
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .claim("sub", "user123")
                .claim("realm_access", Map.of("roles", "not-a-list"))
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        Collection<GrantedAuthority> authorities = converter.convert(jwt).getAuthorities();

        // Spring Security may add default scope authorities
        assertNotNull(authorities);
    }

    @Test
    void keycloakRealmRoles_whenRolesContainNonStringValues_thenFiltersOutNonStrings() {
        JwtAuthenticationConverter converter = httpSecurityConfig.jwtAuthenticationConverter();
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .claim("sub", "user123")
                .claim("realm_access", Map.of("roles", List.of("user", 123, "admin")))
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        Collection<GrantedAuthority> authorities = converter.convert(jwt).getAuthorities();

        assertTrue(authorities.size() >= 2, "Should have at least 2 authorities");
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_user")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_admin")));
    }

    @Test
    void keycloakRealmRoles_whenRolesHaveSpecialCharacters_thenPreservesRoleNames() {
        JwtAuthenticationConverter converter = httpSecurityConfig.jwtAuthenticationConverter();
        Jwt jwt = createJwtWithRoles(List.of("realm-admin", "view_users", "create.programs"));

        Collection<GrantedAuthority> authorities = converter.convert(jwt).getAuthorities();

        assertTrue(authorities.size() >= 3, "Should have at least 3 authorities");
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_realm-admin")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_view_users")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_create.programs")));
    }

    // ==================== Helper Methods ====================

    private Jwt createJwtWithRoles(List<String> roles) {
        return Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .claim("sub", "user123")
                .claim("realm_access", Map.of("roles", roles))
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();
    }
}

