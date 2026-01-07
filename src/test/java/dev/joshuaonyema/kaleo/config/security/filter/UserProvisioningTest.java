package dev.joshuaonyema.kaleo.config.security.filter;

import dev.joshuaonyema.kaleo.domain.entity.User;
import dev.joshuaonyema.kaleo.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProvisioningTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private Jwt jwt;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private UserProvisioning userProvisioning;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private UUID userId;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        userId = UUID.randomUUID();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    // ==================== User Provisioning Tests ====================

    @Test
    void doFilterInternal_whenNewUser_thenCreatesUser() throws ServletException, IOException {
        setupSecurityContext();
        when(jwt.getSubject()).thenReturn(userId.toString());
        when(jwt.getClaimAsString("preferred_username")).thenReturn("testuser");
        when(jwt.getClaimAsString("email")).thenReturn("test@example.com");
        when(userRepository.existsById(userId)).thenReturn(false);

        userProvisioning.doFilterInternal(request, response, filterChain);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals(userId, savedUser.getId());
        assertEquals("testuser", savedUser.getName());
        assertEquals("test@example.com", savedUser.getEmail());
    }

    @Test
    void doFilterInternal_whenExistingUser_thenDoesNotCreateUser() throws ServletException, IOException {
        setupSecurityContext();
        when(jwt.getSubject()).thenReturn(userId.toString());
        when(userRepository.existsById(userId)).thenReturn(true);

        userProvisioning.doFilterInternal(request, response, filterChain);

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void doFilterInternal_whenCalled_thenAlwaysContinuesFilterChain() throws ServletException, IOException {
        setupSecurityContext();
        when(jwt.getSubject()).thenReturn(userId.toString());
        when(userRepository.existsById(userId)).thenReturn(true);

        userProvisioning.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_whenNewUser_thenContinuesFilterChainAfterSave() throws ServletException, IOException {
        setupSecurityContext();
        when(jwt.getSubject()).thenReturn(userId.toString());
        when(jwt.getClaimAsString("preferred_username")).thenReturn("testuser");
        when(jwt.getClaimAsString("email")).thenReturn("test@example.com");
        when(userRepository.existsById(userId)).thenReturn(false);

        userProvisioning.doFilterInternal(request, response, filterChain);

        verify(userRepository).save(any(User.class));
        verify(filterChain).doFilter(request, response);
    }

    // ==================== No Authentication Tests ====================

    @Test
    void doFilterInternal_whenNoAuthentication_thenDoesNotCreateUser() throws ServletException, IOException {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(null);

        userProvisioning.doFilterInternal(request, response, filterChain);

        verify(userRepository, never()).existsById(any());
        verify(userRepository, never()).save(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_whenNotAuthenticated_thenDoesNotCreateUser() throws ServletException, IOException {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        userProvisioning.doFilterInternal(request, response, filterChain);

        verify(userRepository, never()).existsById(any());
        verify(userRepository, never()).save(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_whenPrincipalNotJwt_thenDoesNotCreateUser() throws ServletException, IOException {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("not-a-jwt");

        userProvisioning.doFilterInternal(request, response, filterChain);

        verify(userRepository, never()).existsById(any());
        verify(userRepository, never()).save(any());
        verify(filterChain).doFilter(request, response);
    }

    // ==================== JWT Claims Tests ====================

    @Test
    void doFilterInternal_whenJwtHasNullUsername_thenCreatesUserWithNullName() throws ServletException, IOException {
        setupSecurityContext();
        when(jwt.getSubject()).thenReturn(userId.toString());
        when(jwt.getClaimAsString("preferred_username")).thenReturn(null);
        when(jwt.getClaimAsString("email")).thenReturn("test@example.com");
        when(userRepository.existsById(userId)).thenReturn(false);

        userProvisioning.doFilterInternal(request, response, filterChain);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertNull(savedUser.getName());
        assertEquals("test@example.com", savedUser.getEmail());
    }

    @Test
    void doFilterInternal_whenJwtHasNullEmail_thenCreatesUserWithNullEmail() throws ServletException, IOException {
        setupSecurityContext();
        when(jwt.getSubject()).thenReturn(userId.toString());
        when(jwt.getClaimAsString("preferred_username")).thenReturn("testuser");
        when(jwt.getClaimAsString("email")).thenReturn(null);
        when(userRepository.existsById(userId)).thenReturn(false);

        userProvisioning.doFilterInternal(request, response, filterChain);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals("testuser", savedUser.getName());
        assertNull(savedUser.getEmail());
    }

    // ==================== Security Context Not Set Tests ====================

    @Test
    void doFilterInternal_whenSecurityContextCleared_thenContinuesFilterChain() throws ServletException, IOException {
        SecurityContextHolder.clearContext();

        userProvisioning.doFilterInternal(request, response, filterChain);

        verify(userRepository, never()).existsById(any());
        verify(userRepository, never()).save(any());
        verify(filterChain).doFilter(request, response);
    }

    // ==================== Helper Methods ====================

    private void setupSecurityContext() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(jwt);
    }
}

