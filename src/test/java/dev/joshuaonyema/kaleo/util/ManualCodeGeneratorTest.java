package dev.joshuaonyema.kaleo.util;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ManualCodeGeneratorTest {

    @Test
    void generate_whenCalled_thenReturns6CharacterCode() {
        String code = ManualCodeGenerator.generate();

        assertNotNull(code);
        assertEquals(6, code.length());
    }

    @Test
    void generate_whenCalled_thenReturnsAlphanumericCode() {
        String code = ManualCodeGenerator.generate();

        assertTrue(code.matches("^[A-Z2-9]+$"), "Code should only contain uppercase letters (except I, O) and digits (except 0, 1)");
    }

    @Test
    void generate_whenCalledMultipleTimes_thenReturnsUniqueCodesWithHighProbability() {
        Set<String> codes = new HashSet<>();
        int iterations = 1000;

        for (int i = 0; i < iterations; i++) {
            codes.add(ManualCodeGenerator.generate());
        }

        // With 32^6 = 1 billion+ combinations, 1000 codes should all be unique
        assertEquals(iterations, codes.size(), "Generated codes should be unique");
    }

    @Test
    void generate_whenCalled_thenDoesNotContainAmbiguousCharacters() {
        for (int i = 0; i < 100; i++) {
            String code = ManualCodeGenerator.generate();

            assertFalse(code.contains("0"), "Code should not contain '0'");
            assertFalse(code.contains("1"), "Code should not contain '1'");
            assertFalse(code.contains("I"), "Code should not contain 'I'");
            assertFalse(code.contains("O"), "Code should not contain 'O'");
        }
    }

    @Test
    void generate_whenCalled_thenReturnsUppercaseOnly() {
        for (int i = 0; i < 100; i++) {
            String code = ManualCodeGenerator.generate();

            assertEquals(code.toUpperCase(), code, "Code should be uppercase");
        }
    }
}

