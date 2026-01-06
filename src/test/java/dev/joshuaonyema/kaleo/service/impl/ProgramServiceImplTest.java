package dev.joshuaonyema.kaleo.service.impl;

import dev.joshuaonyema.kaleo.domain.entity.PassType;
import dev.joshuaonyema.kaleo.domain.entity.Program;
import dev.joshuaonyema.kaleo.domain.entity.ProgramStatus;
import dev.joshuaonyema.kaleo.domain.entity.User;
import dev.joshuaonyema.kaleo.exception.UserNotFoundException;
import dev.joshuaonyema.kaleo.repository.ProgramRepository;
import dev.joshuaonyema.kaleo.repository.UserRepository;
import dev.joshuaonyema.kaleo.service.request.CreatePassTypeRequest;
import dev.joshuaonyema.kaleo.service.request.CreateProgramRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgramServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProgramRepository programRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private Jwt jwt;

    @InjectMocks
    private ProgramServiceImpl programService;

    private UUID userId;
    private User user;
    private CreateProgramRequest validRequest;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);
        user.setName("Test Organizer");
        user.setEmail("organizer@test.com");

        var now = LocalDateTime.now();
        validRequest = new CreateProgramRequest(
                "Sunday Service",
                now.plusDays(1),
                now.plusDays(1).plusHours(2),
                "Main Hall",
                now.plusHours(1),
                now.plusDays(1),
                ProgramStatus.DRAFT,
                List.of(new CreatePassTypeRequest("General", BigDecimal.TEN, "Standard entry", 100))
        );
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void setupSecurityContext() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(jwt.getSubject()).thenReturn(userId.toString());
    }

    @Test
    void createProgram_whenValidRequest_thenSavesAndReturnsProgram() {
        setupSecurityContext();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(programRepository.save(any(Program.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Program result = programService.createProgram(validRequest);

        assertNotNull(result);
        assertEquals(validRequest.getName(), result.getName());
        assertEquals(validRequest.getStartTime(), result.getStartTime());
        assertEquals(validRequest.getEndTime(), result.getEndTime());
        assertEquals(validRequest.getVenue(), result.getVenue());
        assertEquals(validRequest.getRegistrationStart(), result.getRegistrationStart());
        assertEquals(validRequest.getRegistrationEnd(), result.getRegistrationEnd());
        assertEquals(validRequest.getStatus(), result.getStatus());
        assertEquals(user, result.getOrganizer());

        verify(programRepository).save(any(Program.class));
    }

    @Test
    void createProgram_whenValidRequest_thenCreatesPassTypes() {
        setupSecurityContext();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(programRepository.save(any(Program.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Program result = programService.createProgram(validRequest);

        assertNotNull(result.getPassTypes());
        assertEquals(1, result.getPassTypes().size());

        PassType passType = result.getPassTypes().getFirst();
        assertEquals("General", passType.getName());
        assertEquals(BigDecimal.TEN, passType.getPrice());
        assertEquals("Standard entry", passType.getDescription());
        assertEquals(100, passType.getTotalAvailable());
        assertEquals(result, passType.getProgram());
    }

    @Test
    void createProgram_whenMultiplePassTypes_thenCreatesAll() {
        setupSecurityContext();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(programRepository.save(any(Program.class))).thenAnswer(invocation -> invocation.getArgument(0));

        validRequest.setPassTypes(List.of(
                new CreatePassTypeRequest("General", BigDecimal.TEN, "Standard entry", 100),
                new CreatePassTypeRequest("VIP", BigDecimal.valueOf(50), "VIP access", 20),
                new CreatePassTypeRequest("Free", BigDecimal.ZERO, null, null)
        ));

        Program result = programService.createProgram(validRequest);

        assertEquals(3, result.getPassTypes().size());
        assertTrue(result.getPassTypes().stream().allMatch(pt -> pt.getProgram() == result));
    }

    @Test
    void createProgram_whenUserNotFound_thenThrowsUserNotFoundException() {
        setupSecurityContext();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> programService.createProgram(validRequest)
        );

        assertTrue(exception.getMessage().contains(userId.toString()));
        verify(programRepository, never()).save(any());
    }

    @Test
    void createProgram_whenNotAuthenticated_thenThrowsAccessDeniedException() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(null);

        assertThrows(
                AccessDeniedException.class,
                () -> programService.createProgram(validRequest)
        );

        verify(programRepository, never()).save(any());
    }

    @Test
    void createProgram_whenPrincipalNotJwt_thenThrowsAccessDeniedException() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("not-a-jwt");

        assertThrows(
                AccessDeniedException.class,
                () -> programService.createProgram(validRequest)
        );

        verify(programRepository, never()).save(any());
    }

    @Test
    void createProgram_whenCalled_thenSavesProgramWithCorrectData() {
        setupSecurityContext();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(programRepository.save(any(Program.class))).thenAnswer(invocation -> invocation.getArgument(0));

        programService.createProgram(validRequest);

        ArgumentCaptor<Program> programCaptor = ArgumentCaptor.forClass(Program.class);
        verify(programRepository).save(programCaptor.capture());

        Program savedProgram = programCaptor.getValue();
        assertEquals(validRequest.getName(), savedProgram.getName());
        assertEquals(validRequest.getVenue(), savedProgram.getVenue());
        assertEquals(validRequest.getStatus(), savedProgram.getStatus());
        assertEquals(user, savedProgram.getOrganizer());
    }

    @Test
    void createProgram_whenOptionalFieldsNull_thenCreatesSuccessfully() {
        setupSecurityContext();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(programRepository.save(any(Program.class))).thenAnswer(invocation -> invocation.getArgument(0));

        validRequest.setRegistrationStart(null);
        validRequest.setRegistrationEnd(null);

        Program result = programService.createProgram(validRequest);

        assertNotNull(result);
        assertNull(result.getRegistrationStart());
        assertNull(result.getRegistrationEnd());
    }

    @Test
    void createProgram_whenPassTypeHasNullOptionalFields_thenCreatesSuccessfully() {
        setupSecurityContext();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(programRepository.save(any(Program.class))).thenAnswer(invocation -> invocation.getArgument(0));

        validRequest.setPassTypes(List.of(
                new CreatePassTypeRequest("Free Entry", BigDecimal.ZERO, null, null)
        ));

        Program result = programService.createProgram(validRequest);

        assertEquals(1, result.getPassTypes().size());
        PassType passType = result.getPassTypes().getFirst();
        assertEquals("Free Entry", passType.getName());
        assertEquals(BigDecimal.ZERO, passType.getPrice());
        assertNull(passType.getDescription());
        assertNull(passType.getTotalAvailable());
    }

    @Test
    void createProgram_whenDifferentStatuses_thenSetsCorrectStatus() {
        setupSecurityContext();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(programRepository.save(any(Program.class))).thenAnswer(invocation -> invocation.getArgument(0));

        for (ProgramStatus status : ProgramStatus.values()) {
            validRequest.setStatus(status);

            Program result = programService.createProgram(validRequest);

            assertEquals(status, result.getStatus());
        }
    }

    @Test
    void createProgram_whenCalled_thenCallsRepositorySaveOnce() {
        setupSecurityContext();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(programRepository.save(any(Program.class))).thenAnswer(invocation -> invocation.getArgument(0));

        programService.createProgram(validRequest);

        verify(programRepository, times(1)).save(any(Program.class));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void createProgram_whenCalled_thenPassTypesAreLinkedToProgram() {
        setupSecurityContext();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(programRepository.save(any(Program.class))).thenAnswer(invocation -> invocation.getArgument(0));

        validRequest.setPassTypes(List.of(
                new CreatePassTypeRequest("General", BigDecimal.TEN, "desc1", 100),
                new CreatePassTypeRequest("VIP", BigDecimal.valueOf(50), "desc2", 20)
        ));

        Program result = programService.createProgram(validRequest);

        for (PassType passType : result.getPassTypes()) {
            assertSame(result, passType.getProgram());
        }
    }

    @Test
    void createProgram_whenSecurityContextNotSet_thenThrowsAccessDeniedException() {
        SecurityContextHolder.clearContext();

        assertThrows(
                AccessDeniedException.class,
                () -> programService.createProgram(validRequest)
        );

        verify(programRepository, never()).save(any());
    }

    @Test
    void createProgram_whenInvalidUuidInJwt_thenThrowsIllegalArgumentException() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(jwt.getSubject()).thenReturn("not-a-valid-uuid");

        assertThrows(
                IllegalArgumentException.class,
                () -> programService.createProgram(validRequest)
        );

        verify(programRepository, never()).save(any());
    }
}