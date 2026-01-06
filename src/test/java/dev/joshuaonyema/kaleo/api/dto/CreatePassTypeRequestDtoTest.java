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

    @Test
    void whenNameIsWhitespaceOnly_thenViolation() {
        var dto = validDto();
        dto.setName("   ");

        var violations = validator.validate(dto);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")));
        assertEquals(1, violations.size());
    }

    @Test
    void whenPriceIsPositive_thenNoViolation() {
        var dto = validDto();
        dto.setPrice(BigDecimal.valueOf(100));

        var violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void whenAllOptionalFieldsAreNull_thenNoViolation() {
        var dto = new CreatePassTypeRequestDto("VIP", BigDecimal.TEN, null, null);

        var violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void whenMultipleFieldsInvalid_thenMultipleViolations() {
        var dto = new CreatePassTypeRequestDto(null, null, null, null);

        var violations = validator.validate(dto);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")));
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("price")));
        assertEquals(2, violations.size());
    }

    @Test
    void whenPriceHasDecimalPlaces_thenNoViolation() {
        var dto = validDto();
        dto.setPrice(new BigDecimal("19.99"));

        var violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void whenPriceIsVeryLarge_thenNoViolation() {
        var dto = validDto();
        dto.setPrice(new BigDecimal("999999999.99"));

        var violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void whenDescriptionIsEmpty_thenNoViolation() {
        var dto = validDto();
        dto.setDescription("");

        var violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void whenTotalAvailableIsZero_thenNoViolation() {
        var dto = validDto();
        dto.setTotalAvailable(0);

        var violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void whenTotalAvailableIsNegative_thenNoViolation() {
        // Note: No validation constraint on totalAvailable, so negative is currently allowed
        var dto = validDto();
        dto.setTotalAvailable(-1);

        var violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }
}

