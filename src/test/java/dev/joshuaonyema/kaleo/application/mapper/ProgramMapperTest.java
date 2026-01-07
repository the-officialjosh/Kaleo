package dev.joshuaonyema.kaleo.application.mapper;

import dev.joshuaonyema.kaleo.api.dto.request.CreatePassTypeRequestDto;
import dev.joshuaonyema.kaleo.api.dto.request.CreateProgramRequestDto;
import dev.joshuaonyema.kaleo.api.dto.response.*;
import dev.joshuaonyema.kaleo.application.command.CreatePassTypeCommand;
import dev.joshuaonyema.kaleo.application.command.CreateProgramCommand;
import dev.joshuaonyema.kaleo.domain.entity.PassType;
import dev.joshuaonyema.kaleo.domain.entity.Program;
import dev.joshuaonyema.kaleo.domain.entity.ProgramStatus;
import dev.joshuaonyema.kaleo.domain.entity.User;
import dev.joshuaonyema.kaleo.mapper.ProgramMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProgramMapperTest {

    private ProgramMapper mapper;

    private UUID programId;
    private UUID passTypeId;
    private LocalDateTime now;
    private Program program;
    private PassType passType;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(ProgramMapper.class);

        programId = UUID.randomUUID();
        passTypeId = UUID.randomUUID();
        now = LocalDateTime.now();

        User organizer = new User();
        organizer.setId(UUID.randomUUID());
        organizer.setName("Test Organizer");
        organizer.setEmail("organizer@test.com");

        passType = new PassType();
        passType.setId(passTypeId);
        passType.setName("General Admission");
        passType.setPrice(BigDecimal.TEN);
        passType.setDescription("Standard entry");
        passType.setTotalAvailable(100);

        program = new Program();
        program.setId(programId);
        program.setName("Sunday Service");
        program.setStartTime(now.plusDays(1));
        program.setEndTime(now.plusDays(1).plusHours(2));
        program.setVenue("Main Hall");
        program.setRegistrationStart(now.plusHours(1));
        program.setRegistrationEnd(now.plusDays(1));
        program.setStatus(ProgramStatus.DRAFT);
        program.setOrganizer(organizer);
        program.setPassTypes(new ArrayList<>(List.of(passType)));

        passType.setProgram(program);
    }

    // ==================== fromDto(CreatePassTypeRequestDto) Tests ====================

    @Test
    void fromDto_whenCreatePassTypeRequestDto_thenMapsAllFields() {
        CreatePassTypeRequestDto dto = new CreatePassTypeRequestDto(
                "VIP Pass",
                BigDecimal.valueOf(50),
                "VIP access",
                20
        );

        CreatePassTypeCommand result = mapper.fromDto(dto);

        assertNotNull(result);
        assertEquals("VIP Pass", result.getName());
        assertEquals(BigDecimal.valueOf(50), result.getPrice());
        assertEquals("VIP access", result.getDescription());
        assertEquals(20, result.getTotalAvailable());
    }

    @Test
    void fromDto_whenCreatePassTypeRequestDtoWithNulls_thenMapsNullFields() {
        CreatePassTypeRequestDto dto = new CreatePassTypeRequestDto(
                "Free Pass",
                BigDecimal.ZERO,
                null,
                null
        );

        CreatePassTypeCommand result = mapper.fromDto(dto);

        assertNotNull(result);
        assertEquals("Free Pass", result.getName());
        assertEquals(BigDecimal.ZERO, result.getPrice());
        assertNull(result.getDescription());
        assertNull(result.getTotalAvailable());
    }

    // ==================== fromDto(CreateProgramRequestDto) Tests ====================

    @Test
    void fromDto_whenCreateProgramRequestDto_thenMapsAllFields() {
        CreateProgramRequestDto dto = new CreateProgramRequestDto();
        dto.setName("Sunday Service");
        dto.setStartTime(now.plusDays(1));
        dto.setEndTime(now.plusDays(1).plusHours(2));
        dto.setVenue("Main Hall");
        dto.setRegistrationStart(now.plusHours(1));
        dto.setRegistrationEnd(now.plusDays(1));
        dto.setStatus(ProgramStatus.DRAFT);
        dto.setPassTypes(List.of(
                new CreatePassTypeRequestDto("General", BigDecimal.TEN, "desc", 100)
        ));

        CreateProgramCommand result = mapper.fromDto(dto);

        assertNotNull(result);
        assertEquals("Sunday Service", result.getName());
        assertEquals(now.plusDays(1), result.getStartTime());
        assertEquals(now.plusDays(1).plusHours(2), result.getEndTime());
        assertEquals("Main Hall", result.getVenue());
        assertEquals(now.plusHours(1), result.getRegistrationStart());
        assertEquals(now.plusDays(1), result.getRegistrationEnd());
        assertEquals(ProgramStatus.DRAFT, result.getStatus());
        assertEquals(1, result.getPassTypes().size());
    }

    @Test
    void fromDto_whenCreateProgramRequestDtoWithNullOptionalFields_thenMapsCorrectly() {
        CreateProgramRequestDto dto = new CreateProgramRequestDto();
        dto.setName("Quick Program");
        dto.setStartTime(now.plusDays(1));
        dto.setEndTime(now.plusDays(1).plusHours(1));
        dto.setVenue("Room 101");
        dto.setRegistrationStart(null);
        dto.setRegistrationEnd(null);
        dto.setStatus(ProgramStatus.PUBLISHED);
        dto.setPassTypes(List.of());

        CreateProgramCommand result = mapper.fromDto(dto);

        assertNotNull(result);
        assertNull(result.getRegistrationStart());
        assertNull(result.getRegistrationEnd());
        assertTrue(result.getPassTypes().isEmpty());
    }

    // ==================== toDto(Program) Tests ====================

    @Test
    void toDto_whenProgram_thenMapsToCreateProgramResponseDto() {
        CreateProgramResponseDto result = mapper.toDto(program);

        assertNotNull(result);
        assertEquals(programId, result.getId());
        assertEquals("Sunday Service", result.getName());
        assertEquals(now.plusDays(1), result.getStartTime());
        assertEquals(now.plusDays(1).plusHours(2), result.getEndTime());
        assertEquals("Main Hall", result.getVenue());
        assertEquals(now.plusHours(1), result.getRegistrationStart());
        assertEquals(now.plusDays(1), result.getRegistrationEnd());
        assertEquals(ProgramStatus.DRAFT, result.getStatus());
    }

    @Test
    void toDto_whenProgramWithPassTypes_thenMapsPassTypes() {
        CreateProgramResponseDto result = mapper.toDto(program);

        assertNotNull(result.getPassTypes());
        assertEquals(1, result.getPassTypes().size());
    }

    // ==================== toDto(PassType) Tests ====================

    @Test
    void toDto_whenPassType_thenMapsToListPassTypeResponseDto() {
        ListPassTypeResponseDto result = mapper.toDto(passType);

        assertNotNull(result);
        assertEquals(passTypeId, result.getId());
        assertEquals("General Admission", result.getName());
        assertEquals(BigDecimal.TEN, result.getPrice());
        assertEquals("Standard entry", result.getDescription());
        assertEquals(100, result.getTotalAvailable());
    }

    @Test
    void toDto_whenPassTypeWithNullOptionalFields_thenMapsCorrectly() {
        PassType simplePassType = new PassType();
        simplePassType.setId(UUID.randomUUID());
        simplePassType.setName("Free Entry");
        simplePassType.setPrice(BigDecimal.ZERO);
        simplePassType.setDescription(null);
        simplePassType.setTotalAvailable(null);

        ListPassTypeResponseDto result = mapper.toDto(simplePassType);

        assertNotNull(result);
        assertEquals("Free Entry", result.getName());
        assertEquals(BigDecimal.ZERO, result.getPrice());
        assertNull(result.getDescription());
        assertNull(result.getTotalAvailable());
    }

    // ==================== toListProgramResponseDto Tests ====================

    @Test
    void toListProgramResponseDto_whenProgram_thenMapsCorrectly() {
        ListProgramResponseDto result = mapper.toListProgramResponseDto(program);

        assertNotNull(result);
        assertEquals(programId, result.getId());
        assertEquals("Sunday Service", result.getName());
        assertEquals(now.plusDays(1), result.getStartTime());
        assertEquals(now.plusDays(1).plusHours(2), result.getEndTime());
        assertEquals("Main Hall", result.getVenue());
        assertEquals(ProgramStatus.DRAFT, result.getStatus());
    }

    @Test
    void toListProgramResponseDto_whenProgramWithPassTypes_thenMapsPassTypes() {
        ListProgramResponseDto result = mapper.toListProgramResponseDto(program);

        assertNotNull(result.getPassTypes());
        assertEquals(1, result.getPassTypes().size());
        assertEquals("General Admission", result.getPassTypes().getFirst().getName());
    }

    // ==================== toGetPassTypesResponseDto Tests ====================

    @Test
    void toGetPassTypesResponseDto_whenPassType_thenMapsCorrectly() {
        GetPassTypesResponseDto result = mapper.toGetPassTypesResponseDto(passType);

        assertNotNull(result);
        assertEquals(passTypeId, result.getId());
        assertEquals("General Admission", result.getName());
        assertEquals(BigDecimal.TEN, result.getPrice());
        assertEquals("Standard entry", result.getDescription());
        assertEquals(100, result.getTotalAvailable());
    }

    // ==================== toGetProgramDetailsResponseDto Tests ====================

    @Test
    void toGetProgramDetailsResponseDto_whenProgram_thenMapsAllFields() {
        GetProgramDetailsResponseDto result = mapper.toGetProgramDetailsResponseDto(program);

        assertNotNull(result);
        assertEquals(programId, result.getId());
        assertEquals("Sunday Service", result.getName());
        assertEquals(now.plusDays(1), result.getStartTime());
        assertEquals(now.plusDays(1).plusHours(2), result.getEndTime());
        assertEquals("Main Hall", result.getVenue());
        assertEquals(now.plusHours(1), result.getRegistrationStart());
        assertEquals(now.plusDays(1), result.getRegistrationEnd());
        assertEquals(ProgramStatus.DRAFT, result.getStatus());
    }

    @Test
    void toGetProgramDetailsResponseDto_whenProgramWithPassTypes_thenMapsPassTypes() {
        GetProgramDetailsResponseDto result = mapper.toGetProgramDetailsResponseDto(program);

        assertNotNull(result.getPassTypes());
        assertEquals(1, result.getPassTypes().size());
        assertEquals("General Admission", result.getPassTypes().getFirst().getName());
    }

    // ==================== Null Input Tests ====================

    @Test
    void fromDto_whenCreatePassTypeRequestDtoNull_thenReturnsNull() {
        CreatePassTypeCommand result = mapper.fromDto((CreatePassTypeRequestDto) null);
        assertNull(result);
    }

    @Test
    void fromDto_whenCreateProgramRequestDtoNull_thenReturnsNull() {
        CreateProgramCommand result = mapper.fromDto((CreateProgramRequestDto) null);
        assertNull(result);
    }

    @Test
    void toDto_whenProgramNull_thenReturnsNull() {
        CreateProgramResponseDto result = mapper.toDto((Program) null);
        assertNull(result);
    }

    @Test
    void toDto_whenPassTypeNull_thenReturnsNull() {
        ListPassTypeResponseDto result = mapper.toDto((PassType) null);
        assertNull(result);
    }

    @Test
    void toListProgramResponseDto_whenProgramNull_thenReturnsNull() {
        ListProgramResponseDto result = mapper.toListProgramResponseDto(null);
        assertNull(result);
    }

    @Test
    void toGetPassTypesResponseDto_whenPassTypeNull_thenReturnsNull() {
        GetPassTypesResponseDto result = mapper.toGetPassTypesResponseDto(null);
        assertNull(result);
    }

    @Test
    void toGetProgramDetailsResponseDto_whenProgramNull_thenReturnsNull() {
        GetProgramDetailsResponseDto result = mapper.toGetProgramDetailsResponseDto(null);
        assertNull(result);
    }
}


