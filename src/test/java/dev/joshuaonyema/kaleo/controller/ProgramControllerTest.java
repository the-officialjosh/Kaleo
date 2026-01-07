package dev.joshuaonyema.kaleo.controller;

import dev.joshuaonyema.kaleo.api.controller.ProgramController;
import dev.joshuaonyema.kaleo.api.dto.request.CreatePassTypeRequestDto;
import dev.joshuaonyema.kaleo.api.dto.request.CreateProgramRequestDto;
import dev.joshuaonyema.kaleo.api.dto.response.CreateProgramResponseDto;
import dev.joshuaonyema.kaleo.api.dto.response.GetProgramDetailsResponseDto;
import dev.joshuaonyema.kaleo.api.dto.response.ListProgramResponseDto;
import dev.joshuaonyema.kaleo.application.command.CreateProgramCommand;
import dev.joshuaonyema.kaleo.application.service.ProgramService;
import dev.joshuaonyema.kaleo.domain.entity.Program;
import dev.joshuaonyema.kaleo.domain.entity.ProgramStatus;
import dev.joshuaonyema.kaleo.domain.entity.User;
import dev.joshuaonyema.kaleo.mapper.ProgramMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgramControllerTest {

    @Mock
    private ProgramMapper programMapper;

    @Mock
    private ProgramService programService;

    @InjectMocks
    private ProgramController programController;

    private UUID programId;
    private Program program;
    private CreateProgramRequestDto createProgramRequestDto;
    private CreateProgramResponseDto createProgramResponseDto;
    private ListProgramResponseDto listProgramResponseDto;
    private GetProgramDetailsResponseDto getProgramDetailsResponseDto;

    @BeforeEach
    void setUp() {
        programId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setName("Test Organizer");
        user.setEmail("organizer@test.com");

        var now = LocalDateTime.now();

        program = new Program();
        program.setId(programId);
        program.setName("Sunday Service");
        program.setStartTime(now.plusDays(1));
        program.setEndTime(now.plusDays(1).plusHours(2));
        program.setVenue("Main Hall");
        program.setStatus(ProgramStatus.DRAFT);
        program.setOrganizer(user);

        createProgramRequestDto = new CreateProgramRequestDto();
        createProgramRequestDto.setName("Sunday Service");
        createProgramRequestDto.setStartTime(now.plusDays(1));
        createProgramRequestDto.setEndTime(now.plusDays(1).plusHours(2));
        createProgramRequestDto.setVenue("Main Hall");
        createProgramRequestDto.setStatus(ProgramStatus.DRAFT);
        createProgramRequestDto.setPassTypes(List.of(
                new CreatePassTypeRequestDto("General", BigDecimal.TEN, "Standard entry", 100)
        ));

        createProgramResponseDto = new CreateProgramResponseDto();
        createProgramResponseDto.setId(programId);
        createProgramResponseDto.setName("Sunday Service");
        createProgramResponseDto.setStartTime(now.plusDays(1));
        createProgramResponseDto.setEndTime(now.plusDays(1).plusHours(2));
        createProgramResponseDto.setVenue("Main Hall");
        createProgramResponseDto.setStatus(ProgramStatus.DRAFT);

        listProgramResponseDto = new ListProgramResponseDto();
        listProgramResponseDto.setId(programId);
        listProgramResponseDto.setName("Sunday Service");
        listProgramResponseDto.setStartTime(now.plusDays(1));
        listProgramResponseDto.setEndTime(now.plusDays(1).plusHours(2));
        listProgramResponseDto.setVenue("Main Hall");
        listProgramResponseDto.setStatus(ProgramStatus.DRAFT);

        getProgramDetailsResponseDto = new GetProgramDetailsResponseDto();
        getProgramDetailsResponseDto.setId(programId);
        getProgramDetailsResponseDto.setName("Sunday Service");
        getProgramDetailsResponseDto.setStartTime(now.plusDays(1));
        getProgramDetailsResponseDto.setEndTime(now.plusDays(1).plusHours(2));
        getProgramDetailsResponseDto.setVenue("Main Hall");
        getProgramDetailsResponseDto.setStatus(ProgramStatus.DRAFT);
    }

    // ==================== createProgram Tests ====================

    @Test
    void createProgram_whenValidRequest_thenReturnsCreatedStatus() {
        when(programMapper.fromDto(any(CreateProgramRequestDto.class))).thenReturn(new CreateProgramCommand());
        when(programService.createProgram(any(CreateProgramCommand.class))).thenReturn(program);
        when(programMapper.toDto(any(Program.class))).thenReturn(createProgramResponseDto);

        ResponseEntity<CreateProgramResponseDto> response = programController.createProgram(createProgramRequestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(programId, response.getBody().getId());
        assertEquals("Sunday Service", response.getBody().getName());
        assertEquals("Main Hall", response.getBody().getVenue());
    }

    @Test
    void createProgram_whenCalled_thenMapperAndServiceAreCalled() {
        when(programMapper.fromDto(any(CreateProgramRequestDto.class))).thenReturn(new CreateProgramCommand());
        when(programService.createProgram(any(CreateProgramCommand.class))).thenReturn(program);
        when(programMapper.toDto(any(Program.class))).thenReturn(createProgramResponseDto);

        programController.createProgram(createProgramRequestDto);

        verify(programMapper, times(1)).fromDto(any(CreateProgramRequestDto.class));
        verify(programService, times(1)).createProgram(any(CreateProgramCommand.class));
        verify(programMapper, times(1)).toDto(any(Program.class));
    }

    @Test
    void createProgram_whenCalled_thenReturnsResponseDto() {
        when(programMapper.fromDto(any(CreateProgramRequestDto.class))).thenReturn(new CreateProgramCommand());
        when(programService.createProgram(any(CreateProgramCommand.class))).thenReturn(program);
        when(programMapper.toDto(any(Program.class))).thenReturn(createProgramResponseDto);

        ResponseEntity<CreateProgramResponseDto> response = programController.createProgram(createProgramRequestDto);

        assertNotNull(response.getBody());
        assertEquals(ProgramStatus.DRAFT, response.getBody().getStatus());
    }

    // ==================== listPrograms Tests ====================

    @Test
    void listPrograms_whenCalled_thenReturnsOkWithPageOfPrograms() {
        Page<Program> programPage = new PageImpl<>(List.of(program));
        Pageable pageable = PageRequest.of(0, 10);
        when(programService.listProgamsForOrganizer(pageable)).thenReturn(programPage);
        when(programMapper.toListProgramResponseDto(any(Program.class))).thenReturn(listProgramResponseDto);

        ResponseEntity<Page<ListProgramResponseDto>> response = programController.listPrograms(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals(programId, response.getBody().getContent().getFirst().getId());
    }

    @Test
    void listPrograms_whenNoPrograms_thenReturnsEmptyPage() {
        Page<Program> emptyPage = Page.empty();
        Pageable pageable = PageRequest.of(0, 10);
        when(programService.listProgamsForOrganizer(pageable)).thenReturn(emptyPage);

        ResponseEntity<Page<ListProgramResponseDto>> response = programController.listPrograms(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().getTotalElements());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void listPrograms_whenPaginationProvided_thenUsesPageable() {
        Pageable pageable = PageRequest.of(2, 5);
        Page<Program> programPage = new PageImpl<>(List.of(program), pageable, 15);
        when(programService.listProgamsForOrganizer(pageable)).thenReturn(programPage);
        when(programMapper.toListProgramResponseDto(any(Program.class))).thenReturn(listProgramResponseDto);

        ResponseEntity<Page<ListProgramResponseDto>> response = programController.listPrograms(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getNumber());
        assertEquals(5, response.getBody().getSize());
        assertEquals(15, response.getBody().getTotalElements());
    }

    @Test
    void listPrograms_whenMultiplePrograms_thenReturnsAllMapped() {
        Program program2 = new Program();
        program2.setId(UUID.randomUUID());
        program2.setName("Wednesday Service");

        ListProgramResponseDto listDto2 = new ListProgramResponseDto();
        listDto2.setId(program2.getId());
        listDto2.setName("Wednesday Service");

        Page<Program> programPage = new PageImpl<>(List.of(program, program2));
        Pageable pageable = PageRequest.of(0, 10);
        when(programService.listProgamsForOrganizer(pageable)).thenReturn(programPage);
        when(programMapper.toListProgramResponseDto(program)).thenReturn(listProgramResponseDto);
        when(programMapper.toListProgramResponseDto(program2)).thenReturn(listDto2);

        ResponseEntity<Page<ListProgramResponseDto>> response = programController.listPrograms(pageable);

        assertEquals(2, response.getBody().getTotalElements());
        verify(programMapper, times(2)).toListProgramResponseDto(any(Program.class));
    }

    // ==================== getProgram Tests ====================

    @Test
    void getProgram_whenProgramExists_thenReturnsOkWithProgram() {
        when(programService.getProgramForOrganizer(programId)).thenReturn(Optional.of(program));
        when(programMapper.toGetProgramDetailsResponseDto(program)).thenReturn(getProgramDetailsResponseDto);

        ResponseEntity<GetProgramDetailsResponseDto> response = programController.getProgram(programId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(programId, response.getBody().getId());
        assertEquals("Sunday Service", response.getBody().getName());
        assertEquals("Main Hall", response.getBody().getVenue());
    }

    @Test
    void getProgram_whenProgramNotFound_thenReturnsNotFound() {
        when(programService.getProgramForOrganizer(programId)).thenReturn(Optional.empty());

        ResponseEntity<GetProgramDetailsResponseDto> response = programController.getProgram(programId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(programMapper, never()).toGetProgramDetailsResponseDto(any());
    }

    @Test
    void getProgram_whenCalled_thenCallsServiceWithCorrectId() {
        UUID specificId = UUID.randomUUID();
        when(programService.getProgramForOrganizer(specificId)).thenReturn(Optional.empty());

        programController.getProgram(specificId);

        verify(programService).getProgramForOrganizer(specificId);
    }

    @Test
    void getProgram_whenProgramExists_thenCallsMapper() {
        when(programService.getProgramForOrganizer(programId)).thenReturn(Optional.of(program));
        when(programMapper.toGetProgramDetailsResponseDto(program)).thenReturn(getProgramDetailsResponseDto);

        programController.getProgram(programId);

        verify(programService).getProgramForOrganizer(programId);
        verify(programMapper).toGetProgramDetailsResponseDto(program);
    }
}

