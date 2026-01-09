package dev.joshuaonyema.kaleo.mapper;

import dev.joshuaonyema.kaleo.api.dto.response.PassValidationResponseDto;
import dev.joshuaonyema.kaleo.domain.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PassValidationMapperTest {

    private PassValidationMapper mapper;

    private UUID passId;
    private UUID passValidationId;
    private Pass pass;
    private PassValidation passValidation;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(PassValidationMapper.class);

        passId = UUID.randomUUID();
        passValidationId = UUID.randomUUID();

        pass = new Pass();
        pass.setId(passId);
        pass.setStatus(PassStatus.ACTIVE);
        pass.setManualCode("ABC123");

        passValidation = new PassValidation();
        passValidation.setId(passValidationId);
        passValidation.setPass(pass);
        passValidation.setPassStatus(PassValidationStatus.VALID);
        passValidation.setValidationMethod(PassValidationMethod.QR_SCAN);
    }

    // ==================== toDto Tests ====================

    @Test
    void toDto_whenPassValidation_thenMapsAllFields() {
        PassValidationResponseDto result = mapper.toDto(passValidation);

        assertNotNull(result);
        assertEquals(passId, result.getPassId());
        assertEquals(PassValidationStatus.VALID, result.getStatus());
    }

    @Test
    void toDto_whenPassValidationWithInvalidStatus_thenMapsStatus() {
        passValidation.setPassStatus(PassValidationStatus.INVALID);

        PassValidationResponseDto result = mapper.toDto(passValidation);

        assertNotNull(result);
        assertEquals(PassValidationStatus.INVALID, result.getStatus());
    }

    @Test
    void toDto_whenPassValidationWithManualMethod_thenMapsCorrectly() {
        passValidation.setValidationMethod(PassValidationMethod.MANUAL);

        PassValidationResponseDto result = mapper.toDto(passValidation);

        assertNotNull(result);
        assertEquals(passId, result.getPassId());
    }

    @Test
    void toDto_whenPassValidationNull_thenReturnsNull() {
        PassValidationResponseDto result = mapper.toDto(null);

        assertNull(result);
    }

    @Test
    void toDto_whenPassIsNull_thenPassIdIsNull() {
        passValidation.setPass(null);

        PassValidationResponseDto result = mapper.toDto(passValidation);

        assertNotNull(result);
        assertNull(result.getPassId());
        assertEquals(PassValidationStatus.VALID, result.getStatus());
    }
}

