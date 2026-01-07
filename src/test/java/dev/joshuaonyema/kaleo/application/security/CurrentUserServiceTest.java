package dev.joshuaonyema.kaleo.application.security;

import dev.joshuaonyema.kaleo.domain.entity.User;
import dev.joshuaonyema.kaleo.exception.UserNotFoundException;
import dev.joshuaonyema.kaleo.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrentUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private Jwt jwt;

    @InjectMocks
    private CurrentUserService currentUserService;

    private UUID userId;
    private User user;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);
        user.setName("Test User");
        user.setEmail("test@example.com");
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    // ==================== getCurrentUser Tests ====================

    @Test
    void getCurrentUser_whenUserExists_thenReturnsUser() {
        setupSecurityContext();
        when(jwt.getSubject()).thenReturn(userId.toString());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = currentUserService.getCurrentUser();

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("Test User", result.getName());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).findById(userId);
    }

    @Test
    void getCurrentUser_whenUserNotFound_thenThrowsUserNotFoundException() {
        setupSecurityContext();
        when(jwt.getSubject()).thenReturn(userId.toString());
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> currentUserService.getCurrentUser()
        );

        assertTrue(exception.getMessage().contains(userId.toString()));
        verify(userRepository).findById(userId);
    }

    @Test
    void getCurrentUser_whenNotAuthenticated_thenThrowsAuthenticationCredentialsNotFoundException() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(null);

        assertThrows(
                AuthenticationCredentialsNotFoundException.class,
                () -> currentUserService.getCurrentUser()
        );

        verify(userRepository, never()).findById(any());
    }

    // ==================== getCurrentUserId Tests ====================

    @Test
    void getCurrentUserId_whenValidAuthentication_thenReturnsUserId() {
        setupSecurityContext();
        when(jwt.getSubject()).thenReturn(userId.toString());

        UUID result = currentUserService.getCurrentUserId();

        assertEquals(userId, result);
    }

    @Test
    void getCurrentUserId_whenNotAuthenticated_thenThrowsAuthenticationCredentialsNotFoundException() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(null);

        assertThrows(
                AuthenticationCredentialsNotFoundException.class,
                () -> currentUserService.getCurrentUserId()
        );
    }

    @Test
    void getCurrentUserId_whenAuthenticationNotAuthenticated_thenThrowsAuthenticationCredentialsNotFoundException() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        assertThrows(
                AuthenticationCredentialsNotFoundException.class,
                () -> currentUserService.getCurrentUserId()
        );
    }

    @Test
    void getCurrentUserId_whenPrincipalNotJwt_thenThrowsAuthenticationCredentialsNotFoundException() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("not-a-jwt");

        assertThrows(
                AuthenticationCredentialsNotFoundException.class,
                () -> currentUserService.getCurrentUserId()
        );
    }

    @Test
    void getCurrentUserId_whenInvalidUuidInJwt_thenThrowsAuthenticationCredentialsNotFoundException() {
        setupSecurityContext();
        when(jwt.getSubject()).thenReturn("not-a-valid-uuid");

        AuthenticationCredentialsNotFoundException exception = assertThrows(
                AuthenticationCredentialsNotFoundException.class,
                () -> currentUserService.getCurrentUserId()
        );

        assertTrue(exception.getMessage().contains("Invalid token subject"));
    }

    @Test
    void getCurrentUserId_whenSecurityContextNotSet_thenThrowsAuthenticationCredentialsNotFoundException() {
        SecurityContextHolder.clearContext();

        assertThrows(
                AuthenticationCredentialsNotFoundException.class,
                () -> currentUserService.getCurrentUserId()
        );
    }

    @Test
    void getCurrentUserId_whenMultipleCalls_thenReturnsConsistentResult() {
        setupSecurityContext();
        when(jwt.getSubject()).thenReturn(userId.toString());

        UUID result1 = currentUserService.getCurrentUserId();
        UUID result2 = currentUserService.getCurrentUserId();

        assertEquals(result1, result2);
        assertEquals(userId, result1);
    }

    // ==================== Helper Methods ====================

    private void setupSecurityContext() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(jwt);
    }
}

