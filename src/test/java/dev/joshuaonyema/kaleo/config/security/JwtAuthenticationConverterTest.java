package dev.joshuaonyema.kaleo.config.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtAuthenticationConverterTest {

    private JwtAuthenticationConverter converter;

    @BeforeEach
    void setUp() {
        converter = new JwtAuthenticationConverter();
    }

    // ==================== Component Annotation Tests ====================

    @Test
    void jwtAuthenticationConverter_shouldHaveComponentAnnotation() {
        Component annotation = AnnotationUtils.findAnnotation(
                JwtAuthenticationConverter.class, Component.class);

        assertNotNull(annotation, "JwtAuthenticationConverter should have @Component annotation");
    }

    // ==================== Convert Method Tests ====================

    @Test
    void convert_whenCalled_thenReturnsJwtAuthenticationToken() {
        Jwt jwt = createJwtWithRoles(List.of("ROLE_user"));

        JwtAuthenticationToken result = converter.convert(jwt);

        assertNotNull(result);
        assertEquals(jwt, result.getToken());
    }

    @Test
    void convert_whenJwtHasValidRolesWithRolePrefix_thenExtractsRoles() {
        Jwt jwt = createJwtWithRoles(List.of("ROLE_user", "ROLE_admin", "ROLE_moderator"));

        Collection<GrantedAuthority> authorities = converter.convert(jwt).getAuthorities();

        assertEquals(3, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_user")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_admin")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_moderator")));
    }

    @Test
    void convert_whenJwtHasSingleRole_thenExtractsSingleRole() {
        Jwt jwt = createJwtWithRoles(List.of("ROLE_ORGANIZER"));

        Collection<GrantedAuthority> authorities = converter.convert(jwt).getAuthorities();

        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ORGANIZER")));
    }

    @Test
    void convert_whenJwtHasRolesWithoutRolePrefix_thenFiltersThemOut() {
        // The converter only keeps roles that START with "ROLE_"
        Jwt jwt = createJwtWithRoles(List.of("user", "admin", "ROLE_moderator"));

        Collection<GrantedAuthority> authorities = converter.convert(jwt).getAuthorities();

        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_moderator")));
        assertFalse(authorities.contains(new SimpleGrantedAuthority("user")));
        assertFalse(authorities.contains(new SimpleGrantedAuthority("admin")));
    }

    @Test
    void convert_whenJwtHasEmptyRolesList_thenReturnsEmptyAuthorities() {
        Jwt jwt = createJwtWithRoles(List.of());

        Collection<GrantedAuthority> authorities = converter.convert(jwt).getAuthorities();

        assertTrue(authorities.isEmpty());
    }

    @Test
    void convert_whenJwtHasNoRealmAccess_thenReturnsEmptyAuthorities() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .claim("sub", "user123")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        Collection<GrantedAuthority> authorities = converter.convert(jwt).getAuthorities();

        assertTrue(authorities.isEmpty());
    }

    @Test
    void convert_whenRealmAccessIsNull_thenReturnsEmptyAuthorities() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .claim("sub", "user123")
                .claim("realm_access", null)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        Collection<GrantedAuthority> authorities = converter.convert(jwt).getAuthorities();

        assertTrue(authorities.isEmpty());
    }

    @Test
    void convert_whenRealmAccessHasNoRolesKey_thenReturnsEmptyAuthorities() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .claim("sub", "user123")
                .claim("realm_access", Map.of("other_field", "value"))
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        Collection<GrantedAuthority> authorities = converter.convert(jwt).getAuthorities();

        assertTrue(authorities.isEmpty());
    }

    @Test
    void convert_whenRolesHaveSpecialCharacters_thenPreservesRoleNames() {
        Jwt jwt = createJwtWithRoles(List.of("ROLE_realm-admin", "ROLE_view_users", "ROLE_create.programs"));

        Collection<GrantedAuthority> authorities = converter.convert(jwt).getAuthorities();

        assertEquals(3, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_realm-admin")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_view_users")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_create.programs")));
    }

    @Test
    void convert_whenMixedRolesWithAndWithoutPrefix_thenOnlyKeepsRolesWithPrefix() {
        Jwt jwt = createJwtWithRoles(List.of("ROLE_ORGANIZER", "attendee", "ROLE_ADMIN", "guest"));

        Collection<GrantedAuthority> authorities = converter.convert(jwt).getAuthorities();

        assertEquals(2, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ORGANIZER")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    void convert_whenAllRolesLackPrefix_thenReturnsEmptyAuthorities() {
        Jwt jwt = createJwtWithRoles(List.of("user", "admin", "organizer"));

        Collection<GrantedAuthority> authorities = converter.convert(jwt).getAuthorities();

        assertTrue(authorities.isEmpty());
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

