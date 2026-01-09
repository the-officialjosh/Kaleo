package dev.joshuaonyema.kaleo.mapper;

import dev.joshuaonyema.kaleo.api.dto.response.GetPassResponseDto;
import dev.joshuaonyema.kaleo.api.dto.response.ListPassResponseDto;
import dev.joshuaonyema.kaleo.domain.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PassMapperTest {

    private PassMapper mapper;

    private UUID passId;
    private UUID passTypeId;
    private UUID programId;
    private LocalDateTime now;
    private Pass pass;
    private PassType passType;
    private Program program;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(PassMapper.class);

        passId = UUID.randomUUID();
        passTypeId = UUID.randomUUID();
        programId = UUID.randomUUID();
        now = LocalDateTime.now();

        program = new Program();
        program.setId(programId);
        program.setName("Sunday Service");
        program.setStartTime(now.plusDays(1));
        program.setEndTime(now.plusDays(1).plusHours(2));
        program.setVenue("Main Hall");
        program.setStatus(ProgramStatus.PUBLISHED);

        passType = new PassType();
        passType.setId(passTypeId);
        passType.setName("General Admission");
        passType.setPrice(BigDecimal.TEN);
        passType.setDescription("Standard entry");
        passType.setTotalAvailable(100);
        passType.setProgram(program);

        pass = new Pass();
        pass.setId(passId);
        pass.setStatus(PassStatus.ACTIVE);
        pass.setPassType(passType);
    }

    // ==================== toListPassResponseDto Tests ====================

    @Test
    void toListPassResponseDto_whenPass_thenMapsAllFields() {
        ListPassResponseDto result = mapper.toListPassResponseDto(pass);

        assertNotNull(result);
        assertEquals(passId, result.getId());
        assertEquals(PassStatus.ACTIVE, result.getStatus());
    }

    @Test
    void toListPassResponseDto_whenPass_thenMapsPassTypeFields() {
        ListPassResponseDto result = mapper.toListPassResponseDto(pass);

        assertNotNull(result);
        assertEquals("General Admission", result.getPassTypeName());
        assertEquals(BigDecimal.TEN, result.getPassTypePrice());
        assertEquals("Standard entry", result.getPassTypeDescription());
    }

    @Test
    void toListPassResponseDto_whenPass_thenMapsProgramFields() {
        ListPassResponseDto result = mapper.toListPassResponseDto(pass);

        assertNotNull(result);
        assertEquals("Sunday Service", result.getProgramName());
        assertEquals(now.plusDays(1), result.getProgramStartTime());
        assertEquals(now.plusDays(1).plusHours(2), result.getProgramEndTime());
        assertEquals("Main Hall", result.getProgramVenue());
    }

    @Test
    void toListPassResponseDto_whenPassWithNullPassType_thenMapsNullFields() {
        pass.setPassType(null);

        ListPassResponseDto result = mapper.toListPassResponseDto(pass);

        assertNotNull(result);
        assertEquals(passId, result.getId());
        assertNull(result.getPassTypeName());
        assertNull(result.getPassTypePrice());
        assertNull(result.getProgramName());
    }

    @Test
    void toListPassResponseDto_whenPassNull_thenReturnsNull() {
        ListPassResponseDto result = mapper.toListPassResponseDto(null);
        assertNull(result);
    }

    // ==================== toGetPassResponseDto Tests ====================

    @Test
    void toGetPassResponseDto_whenPass_thenMapsAllFields() {
        GetPassResponseDto result = mapper.toGetPassResponseDto(pass);

        assertNotNull(result);
        assertEquals(passId, result.getId());
        assertEquals(PassStatus.ACTIVE, result.getStatus());
    }

    @Test
    void toGetPassResponseDto_whenPass_thenMapsPassTypeFields() {
        GetPassResponseDto result = mapper.toGetPassResponseDto(pass);

        assertNotNull(result);
        assertEquals("General Admission", result.getPassTypeName());
        assertEquals(BigDecimal.TEN, result.getPassTypePrice());
        assertEquals("Standard entry", result.getPassTypeDescription());
    }

    @Test
    void toGetPassResponseDto_whenPass_thenMapsProgramFields() {
        GetPassResponseDto result = mapper.toGetPassResponseDto(pass);

        assertNotNull(result);
        assertEquals(programId, result.getProgramId());
        assertEquals("Sunday Service", result.getProgramName());
        assertEquals(now.plusDays(1), result.getProgramStartTime());
        assertEquals(now.plusDays(1).plusHours(2), result.getProgramEndTime());
        assertEquals("Main Hall", result.getProgramVenue());
    }

    @Test
    void toGetPassResponseDto_whenPassWithCancelledStatus_thenMapsStatus() {
        pass.setStatus(PassStatus.CANCELLED);

        GetPassResponseDto result = mapper.toGetPassResponseDto(pass);

        assertNotNull(result);
        assertEquals(PassStatus.CANCELLED, result.getStatus());
    }

    @Test
    void toGetPassResponseDto_whenPassWithNullPassType_thenMapsNullFields() {
        pass.setPassType(null);

        GetPassResponseDto result = mapper.toGetPassResponseDto(pass);

        assertNotNull(result);
        assertEquals(passId, result.getId());
        assertNull(result.getPassTypeName());
        assertNull(result.getProgramId());
    }

    @Test
    void toGetPassResponseDto_whenPassNull_thenReturnsNull() {
        GetPassResponseDto result = mapper.toGetPassResponseDto(null);
        assertNull(result);
    }

    // ==================== Edge Cases ====================

    @Test
    void toListPassResponseDto_whenPassTypeHasNullProgram_thenMapsNullProgramFields() {
        passType.setProgram(null);

        ListPassResponseDto result = mapper.toListPassResponseDto(pass);

        assertNotNull(result);
        assertEquals("General Admission", result.getPassTypeName());
        assertNull(result.getProgramName());
        assertNull(result.getProgramVenue());
    }

    @Test
    void toGetPassResponseDto_whenPassTypeHasNullProgram_thenMapsNullProgramFields() {
        passType.setProgram(null);

        GetPassResponseDto result = mapper.toGetPassResponseDto(pass);

        assertNotNull(result);
        assertEquals("General Admission", result.getPassTypeName());
        assertNull(result.getProgramId());
        assertNull(result.getProgramName());
    }

    @Test
    void toListPassResponseDto_whenPassTypeHasNullDescription_thenMapsNull() {
        passType.setDescription(null);

        ListPassResponseDto result = mapper.toListPassResponseDto(pass);

        assertNotNull(result);
        assertNull(result.getPassTypeDescription());
    }

    @Test
    void toGetPassResponseDto_whenPassTypeHasZeroPrice_thenMapsZero() {
        passType.setPrice(BigDecimal.ZERO);

        GetPassResponseDto result = mapper.toGetPassResponseDto(pass);

        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result.getPassTypePrice());
    }
}

