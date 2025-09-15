package com.pedro.aviationapi.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ValidationUtilsTest {

    @Test
    void shouldAcceptValidCodes() {
        assertDoesNotThrow(() -> ValidationUtils.validateAirportsCodes("AVL, KVLM"));
        assertDoesNotThrow(() -> ValidationUtils.validateAirportsCodes("ABC, 1234"));
    }

    @Test
    void shouldFailWhenNullOrBlank() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtils.validateAirportsCodes(null));
        assertEquals("Nenhum código foi informado!", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtils.validateAirportsCodes("   "));
        assertEquals("Nenhum código foi informado!", ex2.getMessage());
    }

    @Test
    void shouldFailWhenCodeHasInvalidLength() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtils.validateAirportsCodes("ABCDE"));
        assertEquals("O código 'ABCDE' deve ter 3 ou 4 caracteres!", ex.getMessage());
    }

    @Test
    void shouldFailWhenCodeHasInvalidCharacters() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtils.validateAirportsCodes("AV@, KVL!"));
        assertEquals("O código 'AV@' deve conter apenas letras maiúsculas e/ou números!", ex.getMessage());
    }
}
