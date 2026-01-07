package dev.joshuaonyema.kaleo.api.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StartBeforeEndValidatorTest {

    private StartBeforeEndValidator validator;

    @BeforeEach
    void setUp() {
        validator = new StartBeforeEndValidator();
    }

    // ==================== Valid Cases Tests ====================

    @Test
    void isValid_whenStartBeforeEnd_thenReturnsTrue() {
        initializeValidator("startTime", "endTime");
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(2);
        TimeRangeObject obj = new TimeRangeObject(start, end);

        assertTrue(validator.isValid(obj, null));
    }

    @Test
    void isValid_whenStartOneMinuteBeforeEnd_thenReturnsTrue() {
        initializeValidator("startTime", "endTime");
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusMinutes(1);
        TimeRangeObject obj = new TimeRangeObject(start, end);

        assertTrue(validator.isValid(obj, null));
    }

    @Test
    void isValid_whenStartOneDayBeforeEnd_thenReturnsTrue() {
        initializeValidator("startTime", "endTime");
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        TimeRangeObject obj = new TimeRangeObject(start, end);

        assertTrue(validator.isValid(obj, null));
    }

    // ==================== Null Cases Tests ====================

    @Test
    void isValid_whenObjectNull_thenReturnsTrue() {
        initializeValidator("startTime", "endTime");

        assertTrue(validator.isValid(null, null));
    }

    @Test
    void isValid_whenStartNull_thenReturnsTrue() {
        initializeValidator("startTime", "endTime");
        LocalDateTime end = LocalDateTime.now();
        TimeRangeObject obj = new TimeRangeObject(null, end);

        assertTrue(validator.isValid(obj, null));
    }

    @Test
    void isValid_whenEndNull_thenReturnsTrue() {
        initializeValidator("startTime", "endTime");
        LocalDateTime start = LocalDateTime.now();
        TimeRangeObject obj = new TimeRangeObject(start, null);

        assertTrue(validator.isValid(obj, null));
    }

    @Test
    void isValid_whenBothNull_thenReturnsTrue() {
        initializeValidator("startTime", "endTime");
        TimeRangeObject obj = new TimeRangeObject(null, null);

        assertTrue(validator.isValid(obj, null));
    }

    // ==================== Invalid Cases Tests ====================

    @Test
    void isValid_whenStartAfterEnd_thenReturnsFalse() {
        initializeValidator("startTime", "endTime");
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.plusHours(2);
        TimeRangeObject obj = new TimeRangeObject(start, end);

        assertFalse(validator.isValid(obj, null));
    }

    @Test
    void isValid_whenStartEqualsEnd_thenReturnsFalse() {
        initializeValidator("startTime", "endTime");
        LocalDateTime time = LocalDateTime.now();
        TimeRangeObject obj = new TimeRangeObject(time, time);

        assertFalse(validator.isValid(obj, null));
    }

    @Test
    void isValid_whenStartOneSecondAfterEnd_thenReturnsFalse() {
        initializeValidator("startTime", "endTime");
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.plusSeconds(1);
        TimeRangeObject obj = new TimeRangeObject(start, end);

        assertFalse(validator.isValid(obj, null));
    }

    // ==================== Invalid Field Names Tests ====================

    @Test
    void isValid_whenStartFieldDoesNotExist_thenReturnsFalse() {
        initializeValidator("nonExistent", "endTime");
        LocalDateTime now = LocalDateTime.now();
        TimeRangeObject obj = new TimeRangeObject(now, now.plusHours(1));

        assertFalse(validator.isValid(obj, null));
    }

    @Test
    void isValid_whenEndFieldDoesNotExist_thenReturnsFalse() {
        initializeValidator("startTime", "nonExistent");
        LocalDateTime now = LocalDateTime.now();
        TimeRangeObject obj = new TimeRangeObject(now, now.plusHours(1));

        assertFalse(validator.isValid(obj, null));
    }

    @Test
    void isValid_whenBothFieldsDoNotExist_thenReturnsFalse() {
        initializeValidator("nonExistent1", "nonExistent2");
        LocalDateTime now = LocalDateTime.now();
        TimeRangeObject obj = new TimeRangeObject(now, now.plusHours(1));

        assertFalse(validator.isValid(obj, null));
    }

    // ==================== Different Field Names Tests ====================

    @Test
    void isValid_whenUsingRegistrationFields_thenWorksCorrectly() {
        initializeValidator("registrationStart", "registrationEnd");
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(7);
        RegistrationObject obj = new RegistrationObject(start, end);

        assertTrue(validator.isValid(obj, null));
    }

    @Test
    void isValid_whenRegistrationStartAfterEnd_thenReturnsFalse() {
        initializeValidator("registrationStart", "registrationEnd");
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.plusDays(1);
        RegistrationObject obj = new RegistrationObject(start, end);

        assertFalse(validator.isValid(obj, null));
    }

    // ==================== Helper Methods ====================

    private void initializeValidator(String startField, String endField) {
        StartBeforeEnd annotation = new StartBeforeEnd() {
            @Override
            public String message() {
                return "start must be before end";
            }

            @Override
            public Class<?>[] groups() {
                return new Class[0];
            }

            @Override
            public Class<? extends jakarta.validation.Payload>[] payload() {
                return new Class[0];
            }

            @Override
            public String startField() {
                return startField;
            }

            @Override
            public String endField() {
                return endField;
            }

            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return StartBeforeEnd.class;
            }
        };
        validator.initialize(annotation);
    }

    // ==================== Test Classes ====================

    private static class TimeRangeObject {
        private final LocalDateTime startTime;
        private final LocalDateTime endTime;

        TimeRangeObject(LocalDateTime startTime, LocalDateTime endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }

    private static class RegistrationObject {
        private final LocalDateTime registrationStart;
        private final LocalDateTime registrationEnd;

        RegistrationObject(LocalDateTime registrationStart, LocalDateTime registrationEnd) {
            this.registrationStart = registrationStart;
            this.registrationEnd = registrationEnd;
        }
    }
}

