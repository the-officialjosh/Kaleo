package dev.joshuaonyema.kaleo.api.controller;

import dev.joshuaonyema.kaleo.api.dto.response.GetPassResponseDto;
import dev.joshuaonyema.kaleo.api.dto.response.ListPassResponseDto;
import dev.joshuaonyema.kaleo.application.service.PassService;
import dev.joshuaonyema.kaleo.application.service.QrCodeService;
import dev.joshuaonyema.kaleo.domain.entity.Pass;
import dev.joshuaonyema.kaleo.domain.entity.PassStatus;
import dev.joshuaonyema.kaleo.domain.entity.PassType;
import dev.joshuaonyema.kaleo.domain.entity.Program;
import dev.joshuaonyema.kaleo.mapper.PassMapper;
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
import org.springframework.http.MediaType;
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
class PassControllerTest {

    @Mock
    private PassService passService;

    @Mock
    private PassMapper passMapper;

    @Mock
    private QrCodeService qrCodeService;

    @InjectMocks
    private PassController passController;

    private UUID passId;
    private UUID programId;
    private Pass pass;
    private ListPassResponseDto listPassResponseDto;
    private GetPassResponseDto getPassResponseDto;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        passId = UUID.randomUUID();
        programId = UUID.randomUUID();
        now = LocalDateTime.now();

        Program program = new Program();
        program.setId(programId);
        program.setName("Sunday Service");
        program.setStartTime(now.plusDays(1));
        program.setEndTime(now.plusDays(1).plusHours(2));
        program.setVenue("Main Hall");

        PassType passType = new PassType();
        passType.setId(UUID.randomUUID());
        passType.setName("General Admission");
        passType.setPrice(BigDecimal.TEN);
        passType.setDescription("Standard entry");
        passType.setProgram(program);

        pass = new Pass();
        pass.setId(passId);
        pass.setStatus(PassStatus.ACTIVE);
        pass.setManualCode("ABC123");
        pass.setPassType(passType);

        listPassResponseDto = new ListPassResponseDto();
        listPassResponseDto.setId(passId);
        listPassResponseDto.setStatus(PassStatus.ACTIVE);
        listPassResponseDto.setPassTypeName("General Admission");
        listPassResponseDto.setPassTypePrice(BigDecimal.TEN);
        listPassResponseDto.setPassTypeDescription("Standard entry");
        listPassResponseDto.setProgramName("Sunday Service");
        listPassResponseDto.setProgramStartTime(now.plusDays(1));
        listPassResponseDto.setProgramEndTime(now.plusDays(1).plusHours(2));
        listPassResponseDto.setProgramVenue("Main Hall");

        getPassResponseDto = new GetPassResponseDto();
        getPassResponseDto.setId(passId);
        getPassResponseDto.setStatus(PassStatus.ACTIVE);
        getPassResponseDto.setManualCode("ABC123");
        getPassResponseDto.setPassTypeName("General Admission");
        getPassResponseDto.setPassTypePrice(BigDecimal.TEN);
        getPassResponseDto.setPassTypeDescription("Standard entry");
        getPassResponseDto.setProgramId(programId);
        getPassResponseDto.setProgramName("Sunday Service");
        getPassResponseDto.setProgramStartTime(now.plusDays(1));
        getPassResponseDto.setProgramEndTime(now.plusDays(1).plusHours(2));
        getPassResponseDto.setProgramVenue("Main Hall");
    }

    // ==================== listPasses Tests ====================

    @Test
    void listPasses_whenCalled_thenReturnsPageOfPasses() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Pass> passPage = new PageImpl<>(List.of(pass), pageable, 1);

        when(passService.listPassesForUser(pageable)).thenReturn(passPage);
        when(passMapper.toListPassResponseDto(pass)).thenReturn(listPassResponseDto);

        Page<ListPassResponseDto> result = passController.listPasses(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(listPassResponseDto, result.getContent().getFirst());
        verify(passService).listPassesForUser(pageable);
        verify(passMapper).toListPassResponseDto(pass);
    }

    @Test
    void listPasses_whenNoPasses_thenReturnsEmptyPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Pass> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(passService.listPassesForUser(pageable)).thenReturn(emptyPage);

        Page<ListPassResponseDto> result = passController.listPasses(pageable);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
        verify(passService).listPassesForUser(pageable);
        verify(passMapper, never()).toListPassResponseDto(any());
    }

    @Test
    void listPasses_whenMultiplePasses_thenReturnsAllPasses() {
        Pageable pageable = PageRequest.of(0, 10);
        Pass pass2 = new Pass();
        pass2.setId(UUID.randomUUID());
        pass2.setStatus(PassStatus.ACTIVE);

        ListPassResponseDto listDto2 = new ListPassResponseDto();
        listDto2.setId(pass2.getId());

        Page<Pass> passPage = new PageImpl<>(List.of(pass, pass2), pageable, 2);

        when(passService.listPassesForUser(pageable)).thenReturn(passPage);
        when(passMapper.toListPassResponseDto(pass)).thenReturn(listPassResponseDto);
        when(passMapper.toListPassResponseDto(pass2)).thenReturn(listDto2);

        Page<ListPassResponseDto> result = passController.listPasses(pageable);

        assertEquals(2, result.getTotalElements());
        verify(passMapper, times(2)).toListPassResponseDto(any(Pass.class));
    }

    // ==================== getPass Tests ====================

    @Test
    void getPass_whenPassExists_thenReturnsPass() {
        when(passService.getPassForUser(passId)).thenReturn(Optional.of(pass));
        when(passMapper.toGetPassResponseDto(pass)).thenReturn(getPassResponseDto);

        ResponseEntity<GetPassResponseDto> response = passController.getPass(passId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(passId, response.getBody().getId());
        verify(passService).getPassForUser(passId);
        verify(passMapper).toGetPassResponseDto(pass);
    }

    @Test
    void getPass_whenPassNotFound_thenReturnsNotFound() {
        when(passService.getPassForUser(passId)).thenReturn(Optional.empty());

        ResponseEntity<GetPassResponseDto> response = passController.getPass(passId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(passService).getPassForUser(passId);
        verify(passMapper, never()).toGetPassResponseDto(any());
    }

    // ==================== getPassQrCode Tests ====================

    @Test
    void getPassQrCode_whenCalled_thenReturnsQrCodeImage() {
        byte[] qrCodeImage = new byte[]{1, 2, 3, 4, 5};
        when(qrCodeService.getQrCodeImageForUserAndPass(passId)).thenReturn(qrCodeImage);

        ResponseEntity<byte[]> response = passController.getPassQrCode(passId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertArrayEquals(qrCodeImage, response.getBody());
        assertEquals(MediaType.IMAGE_PNG, response.getHeaders().getContentType());
        assertEquals(qrCodeImage.length, response.getHeaders().getContentLength());
        verify(qrCodeService).getQrCodeImageForUserAndPass(passId);
    }

    @Test
    void getPassQrCode_whenCalled_thenSetsCorrectHeaders() {
        byte[] qrCodeImage = new byte[100];
        when(qrCodeService.getQrCodeImageForUserAndPass(passId)).thenReturn(qrCodeImage);

        ResponseEntity<byte[]> response = passController.getPassQrCode(passId);

        assertEquals(MediaType.IMAGE_PNG, response.getHeaders().getContentType());
        assertEquals(100, response.getHeaders().getContentLength());
    }
}

