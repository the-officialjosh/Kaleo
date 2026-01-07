package dev.joshuaonyema.kaleo.api.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BothOrNoneValidatorTest {

    private BothOrNoneValidator validator;

    @BeforeEach
    void setUp() {
        validator = new BothOrNoneValidator();
    }

    // ==================== Both Null Tests ====================

    @Test
    void isValid_whenBothFieldsNull_thenReturnsTrue() {
        initializeValidator("field1", "field2");
        TestObject obj = new TestObject(null, null);

        assertTrue(validator.isValid(obj, null));
    }

    // ==================== Both Present Tests ====================

    @Test
    void isValid_whenBothFieldsPresent_thenReturnsTrue() {
        initializeValidator("field1", "field2");
        TestObject obj = new TestObject("value1", "value2");

        assertTrue(validator.isValid(obj, null));
    }

    @Test
    void isValid_whenBothFieldsPresentWithEmptyStrings_thenReturnsTrue() {
        initializeValidator("field1", "field2");
        TestObject obj = new TestObject("", "");

        assertTrue(validator.isValid(obj, null));
    }

    // ==================== One Null Tests ====================

    @Test
    void isValid_whenFirstFieldNullSecondPresent_thenReturnsFalse() {
        initializeValidator("field1", "field2");
        TestObject obj = new TestObject(null, "value2");

        assertFalse(validator.isValid(obj, null));
    }

    @Test
    void isValid_whenFirstFieldPresentSecondNull_thenReturnsFalse() {
        initializeValidator("field1", "field2");
        TestObject obj = new TestObject("value1", null);

        assertFalse(validator.isValid(obj, null));
    }

    // ==================== Invalid Field Names Tests ====================

    @Test
    void isValid_whenFirstFieldDoesNotExist_thenReturnsFalse() {
        initializeValidator("nonExistent", "field2");
        TestObject obj = new TestObject("value1", "value2");

        assertFalse(validator.isValid(obj, null));
    }

    @Test
    void isValid_whenSecondFieldDoesNotExist_thenReturnsFalse() {
        initializeValidator("field1", "nonExistent");
        TestObject obj = new TestObject("value1", "value2");

        assertFalse(validator.isValid(obj, null));
    }

    @Test
    void isValid_whenBothFieldsDoNotExist_thenReturnsFalse() {
        initializeValidator("nonExistent1", "nonExistent2");
        TestObject obj = new TestObject("value1", "value2");

        assertFalse(validator.isValid(obj, null));
    }

    // ==================== Different Types Tests ====================

    @Test
    void isValid_whenFieldsAreDifferentTypes_thenWorksCorrectly() {
        initializeValidator("stringField", "integerField");
        MixedTypeObject obj = new MixedTypeObject("value", 123);

        assertTrue(validator.isValid(obj, null));
    }

    @Test
    void isValid_whenMixedTypesBothNull_thenReturnsTrue() {
        initializeValidator("stringField", "integerField");
        MixedTypeObject obj = new MixedTypeObject(null, null);

        assertTrue(validator.isValid(obj, null));
    }

    @Test
    void isValid_whenMixedTypesOneNull_thenReturnsFalse() {
        initializeValidator("stringField", "integerField");
        MixedTypeObject obj = new MixedTypeObject("value", null);

        assertFalse(validator.isValid(obj, null));
    }

    // ==================== Helper Methods ====================

    private void initializeValidator(String first, String second) {
        BothOrNone annotation = new BothOrNone() {
            @Override
            public String message() {
                return "Both fields must be set or both must be null";
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
            public String first() {
                return first;
            }

            @Override
            public String second() {
                return second;
            }

            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return BothOrNone.class;
            }
        };
        validator.initialize(annotation);
    }

    // ==================== Test Classes ====================

    private static class TestObject {
        private final String field1;
        private final String field2;

        TestObject(String field1, String field2) {
            this.field1 = field1;
            this.field2 = field2;
        }
    }

    private static class MixedTypeObject {
        private final String stringField;
        private final Integer integerField;

        MixedTypeObject(String stringField, Integer integerField) {
            this.stringField = stringField;
            this.integerField = integerField;
        }
    }
}

