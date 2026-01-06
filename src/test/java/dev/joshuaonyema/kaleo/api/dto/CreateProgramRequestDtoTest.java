package dev.joshuaonyema.kaleo.api.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

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
        dto.setVenue("Spring Church");
        dto.setPassTypes(List.of());

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
        dto.setPassTypes(List.of());

        var validations = validator.validate(dto);

        assertTrue(validations.stream()
                .anyMatch(v -> v.getMessage()
                        .toLowerCase().contains("starttime")));
    }

}