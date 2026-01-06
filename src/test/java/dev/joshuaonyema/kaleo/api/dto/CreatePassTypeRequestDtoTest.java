package dev.joshuaonyema.kaleo.api.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreatePassTypeRequestDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private static CreatePassTypeRequestDto validDto() {
        return new CreatePassTypeRequestDto("General Admission", BigDecimal.TEN, "Standard entry", 100);
    }

    @Test
    void whenAllFieldsValid_thenNoViolation() {
        var dto = validDto();

        var violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void whenNameIsBlank_thenViolation() {
        var dto = validDto();
        dto.setName("");

        var violations = validator.validate(dto);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")));
        assertEquals(1, violations.size());
    }

    @Test
    void whenNameIsNull_thenViolation() {
        var dto = validDto();
        dto.setName(null);

        var violations = validator.validate(dto);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")));
        assertEquals(1, violations.size());
    }

    @Test
    void whenPriceIsNull_thenViolation() {
        var dto = validDto();
        dto.setPrice(null);

        var violations = validator.validate(dto);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("price")));
        assertEquals(1, violations.size());
    }

    @Test
    void whenPriceIsNegative_thenViolation() {
        var dto = validDto();
        dto.setPrice(BigDecimal.valueOf(-1));

        var violations = validator.validate(dto);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("price")));
        assertEquals(1, violations.size());
    }

    @Test
    void whenPriceIsZero_thenNoViolation() {
        var dto = validDto();
        dto.setPrice(BigDecimal.ZERO);

        var violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void whenDescriptionIsNull_thenNoViolation() {
        var dto = validDto();
        dto.setDescription(null);

        var violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void whenTotalAvailableIsNull_thenNoViolation() {
        var dto = validDto();
        dto.setTotalAvailable(null);

        var violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }
}

