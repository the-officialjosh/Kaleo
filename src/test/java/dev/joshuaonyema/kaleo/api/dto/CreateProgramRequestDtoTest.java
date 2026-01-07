package dev.joshuaonyema.kaleo.api.dto;

import dev.joshuaonyema.kaleo.api.dto.request.CreatePassTypeRequestDto;
import dev.joshuaonyema.kaleo.api.dto.request.CreateProgramRequestDto;
import dev.joshuaonyema.kaleo.domain.entity.ProgramStatus;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreateProgramRequestDtoTest {
    private static CreateProgramRequestDto validDto() {
        var now = LocalDateTime.now();
        var dto = new CreateProgramRequestDto();
        dto.setName("Sunday Service");
        dto.setStartTime(now.plusHours(1));
        dto.setEndTime(now.plusHours(2));
        dto.setVenue("Spring Church");
        dto.setStatus(ProgramStatus.DRAFT);
        dto.setPassTypes(List.of(new CreatePassTypeRequestDto("General", BigDecimal.ZERO, null, 100)));
        return dto;
    }

    private static Validator validator;

    @BeforeAll
    static void setupValidator(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void  whenNameBlank_thenViolation(){
        var dto = validDto();
        dto.setName("");

        var violations = validator.validate(dto);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath()
                        .toString().equals("name")));
        assertEquals(1, violations.size());
    }

    @Test
    void whenStartAfterEnd_thenViolation(){
        var dto = validDto();
        var now = LocalDateTime.now();
        dto.setStartTime(now.plusHours(2));
        dto.setEndTime(now.plusHours(1));

        var violations = validator.validate(dto);

        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().isEmpty()));
        assertEquals(1, violations.size());
    }

    @Test
    void whenStartTimeMissing_thenViolation() {
        var dto = validDto();
        dto.setStartTime(null);

        var violations = validator.validate(dto);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath()
                        .toString().equals("startTime")));
        assertEquals(1, violations.size());
    }

    @Test
    void whenEndTimeMissing_thenViolation() {
        var dto = validDto();

        dto.setEndTime(null);

        var violations = validator.validate(dto);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath()
                        .toString().equals("endTime")));
        assertEquals(1, violations.size());
    }

    @Test
    void  whenVenueBlank_thenViolation(){
        var dto = validDto();
        dto.setVenue("");

        var violations = validator.validate(dto);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath()
                        .toString().equals("venue")));
        assertEquals(1, violations.size());
    }

    @Test
    void whenStatusMissing_thenViolation() {
        var dto = validDto();
        dto.setStatus(null);

        var violations = validator.validate(dto);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath()
                        .toString().equals("status")));
        assertEquals(1, violations.size());
    }

    @Test
    void whenPassTypesMissing_thenViolation() {
        var dto = validDto();
        dto.setPassTypes(List.of());

        var violations = validator.validate(dto);

        assertTrue(violations.stream()
                .anyMatch(v ->
                  v.getPropertyPath().toString()
                          .equals("passTypes")
                ));
        assertEquals(1, violations.size());
    }

    @Test
    void whenCreatePassTypeRequestDtoIsNotValid_thenViolation() {
        var dto = validDto();
        dto.setPassTypes(List.of(new CreatePassTypeRequestDto(null, null, null, null)));

        var violations = validator.validate(dto);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath()
                        .toString().equals("passTypes[0].name")));
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath()
                        .toString().equals("passTypes[0].price")));
        assertFalse(violations.stream()
                .anyMatch(v -> v.getPropertyPath()
                        .toString().equals("passTypes[0].description")));
        assertFalse(violations.stream()
                .anyMatch(v -> v.getPropertyPath()
                        .toString().equals("passTypes[0].totalAvailable")));
        assertEquals(2, violations.size());

    }

    @Test
    void whenRegistrationStartAfterRegistrationEnd_thenViolation(){
        var dto = validDto();

        var now = LocalDateTime.now();
        dto.setStartTime(now.plusHours(2));
        dto.setEndTime(now.plusHours(1));

        var violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().isEmpty()));
        assertEquals(1, violations.size());
    }


    @Test
    void whenRegistrationStartIsSetButRegistrationEndIsNull_thenViolation() {
        var dto = validDto();

        dto.setRegistrationStart(LocalDateTime.now().plusHours(1));
        dto.setRegistrationEnd(null);

        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().isEmpty()));
        assertEquals(1, violations.size());
    }

    @Test
    void whenRegistrationEndIsSetButRegistrationStartIsNull_thenViolation() {
        var dto = validDto();

        dto.setRegistrationStart(null);
        dto.setRegistrationEnd(LocalDateTime.now().plusHours(1));

        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().isEmpty()));
        assertEquals(1, violations.size());
    }

    @Test
    void whenBothRegistrationStartAndEndAreNull_thenNoViolation() {
        var dto = validDto();

        dto.setRegistrationStart(null);
        dto.setRegistrationEnd(null);

        var violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void whenAllFieldsIncludingRegistrationWindowAreValid_thenNoViolation() {
        var dto = validDto();
        var now = LocalDateTime.now();
        dto.setRegistrationStart(now.plusMinutes(10));
        dto.setRegistrationEnd(now.plusHours(1));

        var violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }
}