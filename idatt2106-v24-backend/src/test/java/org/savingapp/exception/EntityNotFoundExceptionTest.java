package org.savingapp.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntityNotFoundExceptionTest {
    @Test
    public void testExceptionMessageWithId() {
        Long idValue = 1L;
        EntityNotFoundException exception = new EntityNotFoundException(idValue, String.class);
        assertEquals("The string with id " + idValue + " does not exist in our records", exception.getMessage());
    }

    @Test
    public void testExceptionMessageWithoutId() {
        EntityNotFoundException exception = new EntityNotFoundException(String.class);
        assertEquals("The string does not exist in our records", exception.getMessage());
    }
}
