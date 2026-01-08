package dev.joshuaonyema.kaleo.api.controller;

import dev.joshuaonyema.kaleo.api.dto.response.ListPublishedProgramResponseDto;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublishedProgramControllerTest {

    @Mock
    private ProgramMapper programMapper;

    @Mock
    private ProgramService programService;

    @InjectMocks
    private PublishedProgramController publishedProgramController;

    private UUID programId;
    private Program program;
    private ListPublishedProgramResponseDto listPublishedProgramResponseDto;

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
        program.setStatus(ProgramStatus.PUBLISHED);
        program.setOrganizer(user);

        listPublishedProgramResponseDto = new ListPublishedProgramResponseDto();
        listPublishedProgramResponseDto.setId(programId);
        listPublishedProgramResponseDto.setName("Sunday Service");
        listPublishedProgramResponseDto.setStartTime(now.plusDays(1));
        listPublishedProgramResponseDto.setEndTime(now.plusDays(1).plusHours(2));
        listPublishedProgramResponseDto.setVenue("Main Hall");
    }

    // ==================== List Published Programs Tests ====================

    @Test
    void listPublishedPrograms_whenProgramsExist_thenReturnsPageOfPrograms() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Program> programPage = new PageImpl<>(List.of(program), pageable, 1);

        when(programService.listPublishedPrograms(any(Pageable.class))).thenReturn(programPage);
        when(programMapper.toListPublishedProgramResponseDto(program)).thenReturn(listPublishedProgramResponseDto);

        ResponseEntity<Page<ListPublishedProgramResponseDto>> response =
                publishedProgramController.listPublishedPrograms(null, pageable);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals(1, response.getBody().getContent().size());

        ListPublishedProgramResponseDto dto = response.getBody().getContent().getFirst();
        assertEquals(programId, dto.getId());
        assertEquals("Sunday Service", dto.getName());
        assertEquals("Main Hall", dto.getVenue());

        verify(programService).listPublishedPrograms(pageable);
        verify(programMapper).toListPublishedProgramResponseDto(program);
    }

    @Test
    void listPublishedPrograms_whenNoProgramsExist_thenReturnsEmptyPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Program> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(programService.listPublishedPrograms(any(Pageable.class))).thenReturn(emptyPage);

        ResponseEntity<Page<ListPublishedProgramResponseDto>> response =
                publishedProgramController.listPublishedPrograms(null, pageable);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().getTotalElements());
        assertTrue(response.getBody().getContent().isEmpty());

        verify(programService).listPublishedPrograms(pageable);
        verify(programMapper, never()).toListPublishedProgramResponseDto(any());
    }

    @Test
    void listPublishedPrograms_whenMultipleProgramsExist_thenReturnsAllPrograms() {
        var now = LocalDateTime.now();

        Program program2 = new Program();
        program2.setId(UUID.randomUUID());
        program2.setName("Evening Service");
        program2.setStartTime(now.plusDays(2));
        program2.setEndTime(now.plusDays(2).plusHours(1));
        program2.setVenue("Chapel");
        program2.setStatus(ProgramStatus.PUBLISHED);

        ListPublishedProgramResponseDto dto2 = new ListPublishedProgramResponseDto();
        dto2.setId(program2.getId());
        dto2.setName("Evening Service");
        dto2.setStartTime(program2.getStartTime());
        dto2.setEndTime(program2.getEndTime());
        dto2.setVenue("Chapel");

        Pageable pageable = PageRequest.of(0, 10);
        Page<Program> programPage = new PageImpl<>(List.of(program, program2), pageable, 2);

        when(programService.listPublishedPrograms(any(Pageable.class))).thenReturn(programPage);
        when(programMapper.toListPublishedProgramResponseDto(program)).thenReturn(listPublishedProgramResponseDto);
        when(programMapper.toListPublishedProgramResponseDto(program2)).thenReturn(dto2);

        ResponseEntity<Page<ListPublishedProgramResponseDto>> response =
                publishedProgramController.listPublishedPrograms(null, pageable);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getTotalElements());
        assertEquals(2, response.getBody().getContent().size());

        verify(programService).listPublishedPrograms(pageable);
        verify(programMapper, times(2)).toListPublishedProgramResponseDto(any());
    }

    @Test
    void listPublishedPrograms_withCustomPageable_thenUsesProvidedPagination() {
        Pageable customPageable = PageRequest.of(1, 5);
        Page<Program> programPage = new PageImpl<>(List.of(program), customPageable, 10);

        when(programService.listPublishedPrograms(customPageable)).thenReturn(programPage);
        when(programMapper.toListPublishedProgramResponseDto(program)).thenReturn(listPublishedProgramResponseDto);

        ResponseEntity<Page<ListPublishedProgramResponseDto>> response =
                publishedProgramController.listPublishedPrograms(null, customPageable);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(10, response.getBody().getTotalElements());
        assertEquals(1, response.getBody().getNumber());
        assertEquals(5, response.getBody().getSize());

        verify(programService).listPublishedPrograms(customPageable);
    }

    @Test
    void listPublishedPrograms_whenServiceReturnsData_thenMapperIsCalledForEachProgram() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Program> programPage = new PageImpl<>(List.of(program), pageable, 1);

        when(programService.listPublishedPrograms(any(Pageable.class))).thenReturn(programPage);
        when(programMapper.toListPublishedProgramResponseDto(any(Program.class)))
                .thenReturn(listPublishedProgramResponseDto);

        ResponseEntity<Page<ListPublishedProgramResponseDto>> response =
                publishedProgramController.listPublishedPrograms(null, pageable);

        assertNotNull(response);
        verify(programMapper, times(1)).toListPublishedProgramResponseDto(program);
    }

    // ==================== Search Published Programs Tests ====================

    @Test
    void listPublishedPrograms_whenSearchQueryProvided_thenCallsSearchMethod() {
        Pageable pageable = PageRequest.of(0, 10);
        String searchQuery = "Sunday";
        Page<Program> programPage = new PageImpl<>(List.of(program), pageable, 1);

        when(programService.searchPublishedPrograms(searchQuery, pageable)).thenReturn(programPage);
        when(programMapper.toListPublishedProgramResponseDto(program)).thenReturn(listPublishedProgramResponseDto);

        ResponseEntity<Page<ListPublishedProgramResponseDto>> response =
                publishedProgramController.listPublishedPrograms(searchQuery, pageable);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());

        verify(programService).searchPublishedPrograms(searchQuery, pageable);
        verify(programService, never()).listPublishedPrograms(any());
        verify(programMapper).toListPublishedProgramResponseDto(program);
    }

    @Test
    void listPublishedPrograms_whenSearchQueryIsEmpty_thenCallsListMethod() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Program> programPage = new PageImpl<>(List.of(program), pageable, 1);

        when(programService.listPublishedPrograms(pageable)).thenReturn(programPage);
        when(programMapper.toListPublishedProgramResponseDto(program)).thenReturn(listPublishedProgramResponseDto);

        ResponseEntity<Page<ListPublishedProgramResponseDto>> response =
                publishedProgramController.listPublishedPrograms("", pageable);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(programService).listPublishedPrograms(pageable);
        verify(programService, never()).searchPublishedPrograms(any(), any());
    }

    @Test
    void listPublishedPrograms_whenSearchQueryIsWhitespace_thenCallsListMethod() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Program> programPage = new PageImpl<>(List.of(program), pageable, 1);

        when(programService.listPublishedPrograms(pageable)).thenReturn(programPage);
        when(programMapper.toListPublishedProgramResponseDto(program)).thenReturn(listPublishedProgramResponseDto);

        ResponseEntity<Page<ListPublishedProgramResponseDto>> response =
                publishedProgramController.listPublishedPrograms("   ", pageable);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(programService).listPublishedPrograms(pageable);
        verify(programService, never()).searchPublishedPrograms(any(), any());
    }

    @Test
    void listPublishedPrograms_whenSearchReturnsNoResults_thenReturnsEmptyPage() {
        Pageable pageable = PageRequest.of(0, 10);
        String searchQuery = "NonExistent";
        Page<Program> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(programService.searchPublishedPrograms(searchQuery, pageable)).thenReturn(emptyPage);

        ResponseEntity<Page<ListPublishedProgramResponseDto>> response =
                publishedProgramController.listPublishedPrograms(searchQuery, pageable);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().getTotalElements());
        assertTrue(response.getBody().getContent().isEmpty());

        verify(programService).searchPublishedPrograms(searchQuery, pageable);
        verify(programMapper, never()).toListPublishedProgramResponseDto(any());
    }
}
