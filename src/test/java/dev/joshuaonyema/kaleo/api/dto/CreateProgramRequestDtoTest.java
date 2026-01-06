package dev.joshuaonyema.kaleo.api.dto;

import dev.joshuaonyema.kaleo.domain.entity.ProgramStatus;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateProgramRequestDtoTest {
    private static Validator validator;

    @BeforeAll
    static void setupValidator(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void  whenNameBlank_thenViolation(){
        var dto = new CreateProgramRequestDto();
        dto.setName("");
        dto.setStartTime(LocalDateTime.now());
        dto.setEndTime(LocalDateTime.now().plusHours(1));
        dto.setVenue("Spring Church");
        dto.setStatus(ProgramStatus.DRAFT);
        dto.setPassTypes(List.of(new CreatePassTypeRequestDto("General", BigInteger.ZERO, null, 100)));

        var violations = validator.validate(dto);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath()
                        .toString().equals("name")));
    }

    @Test
    void whenStartAfterEnd_thenViolation(){
        var dto = new CreateProgramRequestDto();
        dto.setName("Sunday Service");
        dto.setStartTime(LocalDateTime.now().plusHours(2));
        dto.setEndTime(LocalDateTime.now().plusHours(1));
        dto.setVenue("Spring Church");
        dto.setStatus(ProgramStatus.DRAFT);
        dto.setPassTypes(List.of(new CreatePassTypeRequestDto("General", BigInteger.ZERO, null, 100)));


        var validations = validator.validate(dto);

        assertTrue(validations.stream()
                .anyMatch(v -> v.getMessage()
                        .toLowerCase().contains("start")));
    }

    @Test
    void whenEndTimeMissing_thenViolation() {
        var dto = new CreateProgramRequestDto();
        dto.setName("Sunday Service");
        dto.setStartTime(LocalDateTime.now());
        dto.setEndTime(null);
        dto.setVenue("Spring Church");
        dto.setStatus(ProgramStatus.DRAFT);
        dto.setPassTypes(List.of(new CreatePassTypeRequestDto("General", BigInteger.ZERO, null, 100)));

        var violations = validator.validate(dto);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath()
                        .toString().equals("endTime")));
    }

    @Test
    void  whenVenueBlank_thenViolation(){
        var dto = new CreateProgramRequestDto();
        dto.setName("Spring Church");
        dto.setStartTime(LocalDateTime.now());
        dto.setEndTime(LocalDateTime.now().plusHours(1));
        dto.setVenue("");
        dto.setStatus(ProgramStatus.DRAFT);
        dto.setPassTypes(List.of(new CreatePassTypeRequestDto("General", BigInteger.ZERO, null, 100)));

        var violations = validator.validate(dto);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath()
                        .toString().equals("venue")));
    }

    @Test
    void whenStatusMissing_thenViolation() {
        var dto = new CreateProgramRequestDto();
        dto.setName("Sunday Service");
        dto.setStartTime(LocalDateTime.now());
        dto.setEndTime(LocalDateTime.now().plusHours(1));
        dto.setVenue("Spring Church");
        dto.setStatus(null);
        dto.setPassTypes(List.of(new CreatePassTypeRequestDto("General", BigInteger.ZERO, null, 100)));

        var violations = validator.validate(dto);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath()
                        .toString().equals("status")));
    }

    @Test
    void whenPassTypesMissing_thenViolation() {
        var dto = new CreateProgramRequestDto();
        dto.setName("Sunday Service");
        dto.setStartTime(LocalDateTime.now());
        dto.setEndTime(LocalDateTime.now().plusHours(1));
        dto.setVenue("Spring Church");
        dto.setStatus(ProgramStatus.DRAFT);
        dto.setPassTypes(List.of());

        var violations = validator.validate(dto);

        assertTrue(violations.stream()
                .anyMatch(v -> {
                    String string = v.getPropertyPath().toString();
                    System.out.println(string);
                    return string.equals("passTypes");
                }));
    }

    @Test
    void whenCreatePassTypeRequestDtoIsNotValid_thenViolation() {
        var dto = new CreateProgramRequestDto();
        dto.setName("Sunday Service");
        dto.setStartTime(LocalDateTime.now());
        dto.setEndTime(LocalDateTime.now().plusHours(1));
        dto.setVenue("Spring Church");
        dto.setStatus(ProgramStatus.DRAFT);
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

    }

    @Test
    void whenRegistrationStartAfterRegistrationEnd_thenViolation(){
        var dto = new CreateProgramRequestDto();
        dto.setName("Sunday Service");
        dto.setStartTime(LocalDateTime.now().plusHours(1));
        dto.setEndTime(LocalDateTime.now().plusHours(2));
        dto.setVenue("Spring Church");
        dto.setStatus(ProgramStatus.DRAFT);
        dto.setPassTypes(List.of(new CreatePassTypeRequestDto("General", BigInteger.ZERO, null, 100)));
        dto.setRegistrationStart(LocalDateTime.now().plusHours(2));
        dto.setRegistrationEnd(LocalDateTime.now().plusHours(1));

        var validations = validator.validate(dto);

        assertTrue(validations.stream()
                .anyMatch(v -> v.getMessage()
                        .toLowerCase().contains("registration")));
    }

}