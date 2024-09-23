package org.savingapp.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenExpiredExceptionTest {
    @Test
    public void testExceptionMessage() {
        String message = "Test message";
        TokenExpiredException exception = new TokenExpiredException(message);
        assertEquals(message, exception.getMessage());
    }
}
