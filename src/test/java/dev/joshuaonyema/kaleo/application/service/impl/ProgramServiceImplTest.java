package dev.joshuaonyema.kaleo.application.service.impl;

import dev.joshuaonyema.kaleo.application.command.CreatePassTypeCommand;
import dev.joshuaonyema.kaleo.application.command.CreateProgramCommand;
import dev.joshuaonyema.kaleo.application.security.CurrentUserService;
import dev.joshuaonyema.kaleo.domain.entity.PassType;
import dev.joshuaonyema.kaleo.domain.entity.Program;
import dev.joshuaonyema.kaleo.domain.entity.ProgramStatus;
import dev.joshuaonyema.kaleo.domain.entity.User;
import dev.joshuaonyema.kaleo.repository.ProgramRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
    private ProgramRepository programRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private ProgramServiceImpl programService;

    private User user;
    private CreateProgramCommand validRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Test Organizer");
        user.setEmail("organizer@test.com");

        var now = LocalDateTime.now();
        validRequest = new CreateProgramCommand(
                "Sunday Service",
                now.plusDays(1),
                now.plusDays(1).plusHours(2),
                "Main Hall",
                now.plusHours(1),
                now.plusDays(1),
                ProgramStatus.DRAFT,
                List.of(new CreatePassTypeCommand("General", BigDecimal.TEN, "Standard entry", 100))
        );
    }

    @Test
    void createProgram_whenValidRequest_thenSavesAndReturnsProgram() {
        when(currentUserService.getCurrentUser()).thenReturn(user);
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
        // Security context setup no longer needed
        when(currentUserService.getCurrentUser()).thenReturn(user);
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
        // Security context setup no longer needed
        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(programRepository.save(any(Program.class))).thenAnswer(invocation -> invocation.getArgument(0));

        validRequest.setPassTypes(List.of(
                new CreatePassTypeCommand("General", BigDecimal.TEN, "Standard entry", 100),
                new CreatePassTypeCommand("VIP", BigDecimal.valueOf(50), "VIP access", 20),
                new CreatePassTypeCommand("Free", BigDecimal.ZERO, null, null)
        ));

        Program result = programService.createProgram(validRequest);

        assertEquals(3, result.getPassTypes().size());
        assertTrue(result.getPassTypes().stream().allMatch(pt -> pt.getProgram() == result));
    }


    // Note: Authentication and user lookup tests have been moved to CurrentUserServiceTest
    // since CurrentUserService now handles those responsibilities

    @Test
    void createProgram_whenCalled_thenSavesProgramWithCorrectData() {
        // Security context setup no longer needed
        when(currentUserService.getCurrentUser()).thenReturn(user);
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
        // Security context setup no longer needed
        when(currentUserService.getCurrentUser()).thenReturn(user);
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
        // Security context setup no longer needed
        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(programRepository.save(any(Program.class))).thenAnswer(invocation -> invocation.getArgument(0));

        validRequest.setPassTypes(List.of(
                new CreatePassTypeCommand("Free Entry", BigDecimal.ZERO, null, null)
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
        // Security context setup no longer needed
        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(programRepository.save(any(Program.class))).thenAnswer(invocation -> invocation.getArgument(0));

        for (ProgramStatus status : ProgramStatus.values()) {
            validRequest.setStatus(status);

            Program result = programService.createProgram(validRequest);

            assertEquals(status, result.getStatus());
        }
    }

    @Test
    void createProgram_whenCalled_thenCallsRepositorySaveOnce() {
        // Security context setup no longer needed
        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(programRepository.save(any(Program.class))).thenAnswer(invocation -> invocation.getArgument(0));

        programService.createProgram(validRequest);

        verify(programRepository, times(1)).save(any(Program.class));
        verify(currentUserService, times(1)).getCurrentUser();
    }

    @Test
    void createProgram_whenCalled_thenPassTypesAreLinkedToProgram() {
        // Security context setup no longer needed
        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(programRepository.save(any(Program.class))).thenAnswer(invocation -> invocation.getArgument(0));

        validRequest.setPassTypes(List.of(
                new CreatePassTypeCommand("General", BigDecimal.TEN, "desc1", 100),
                new CreatePassTypeCommand("VIP", BigDecimal.valueOf(50), "desc2", 20)
        ));

        Program result = programService.createProgram(validRequest);

        for (PassType passType : result.getPassTypes()) {
            assertSame(result, passType.getProgram());
        }
    }


    // ==================== listProgramsForOrganizer Tests ====================

    @Test
    void listProgramsForOrganizer_whenValidRequest_thenReturnsPageOfPrograms() {
        when(currentUserService.getCurrentUserId()).thenReturn(user.getId());

        Program program1 = createTestProgram("Program 1");
        Program program2 = createTestProgram("Program 2");
        Page<Program> expectedPage = new PageImpl<>(List.of(program1, program2));
        Pageable pageable = PageRequest.of(0, 10);

        when(programRepository.findByOrganizerId(user.getId(), pageable)).thenReturn(expectedPage);

        Page<Program> result = programService.listProgramsForOrganizer(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(programRepository).findByOrganizerId(user.getId(), pageable);
    }

    @Test
    void listProgramsForOrganizer_whenNoPrograms_thenReturnsEmptyPage() {
        when(currentUserService.getCurrentUserId()).thenReturn(user.getId());

        Page<Program> emptyPage = Page.empty();
        Pageable pageable = PageRequest.of(0, 10);

        when(programRepository.findByOrganizerId(user.getId(), pageable)).thenReturn(emptyPage);

        Page<Program> result = programService.listProgramsForOrganizer(pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.isEmpty());
    }


    @Test
    void listProgramsForOrganizer_whenPaginationApplied_thenRespectsPageable() {
        when(currentUserService.getCurrentUserId()).thenReturn(user.getId());

        Program program = createTestProgram("Test Program");
        Pageable pageable = PageRequest.of(2, 5);
        Page<Program> expectedPage = new PageImpl<>(List.of(program), pageable, 15);

        when(programRepository.findByOrganizerId(user.getId(), pageable)).thenReturn(expectedPage);

        Page<Program> result = programService.listProgramsForOrganizer(pageable);

        assertEquals(2, result.getNumber());
        assertEquals(5, result.getSize());
        assertEquals(15, result.getTotalElements());
    }

    // ==================== getProgramForOrganizer Tests ====================

    @Test
    void getProgramForOrganizer_whenProgramExists_thenReturnsProgram() {
        when(currentUserService.getCurrentUserId()).thenReturn(user.getId());

        UUID programId = UUID.randomUUID();
        Program program = createTestProgram("Test Program");
        program.setId(programId);

        when(programRepository.findByIdAndOrganizerId(programId, user.getId())).thenReturn(Optional.of(program));

        Optional<Program> result = programService.getProgramForOrganizer(programId);

        assertTrue(result.isPresent());
        assertEquals(programId, result.get().getId());
        assertEquals("Test Program", result.get().getName());
    }

    @Test
    void getProgramForOrganizer_whenProgramNotFound_thenReturnsEmpty() {
        when(currentUserService.getCurrentUserId()).thenReturn(user.getId());

        UUID programId = UUID.randomUUID();
        when(programRepository.findByIdAndOrganizerId(programId, user.getId())).thenReturn(Optional.empty());

        Optional<Program> result = programService.getProgramForOrganizer(programId);

        assertTrue(result.isEmpty());
    }

    @Test
    void getProgramForOrganizer_whenProgramBelongsToDifferentOrganizer_thenReturnsEmpty() {
        when(currentUserService.getCurrentUserId()).thenReturn(user.getId());

        UUID programId = UUID.randomUUID();
        when(programRepository.findByIdAndOrganizerId(programId, user.getId())).thenReturn(Optional.empty());

        Optional<Program> result = programService.getProgramForOrganizer(programId);

        assertTrue(result.isEmpty());
        verify(programRepository).findByIdAndOrganizerId(programId, user.getId());
    }


    // ==================== Helper Methods ====================

    private Program createTestProgram(String name) {
        Program program = new Program();
        program.setId(UUID.randomUUID());
        program.setName(name);
        program.setOrganizer(user);
        program.setStartTime(LocalDateTime.now().plusDays(1));
        program.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2));
        program.setVenue("Test Venue");
        program.setStatus(ProgramStatus.DRAFT);
        return program;
    }
}

