package org.savingapp.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorResponseTest {
    private ErrorResponse errorResponse;

    @BeforeEach
    public void setup() {
        errorResponse = new ErrorResponse("Test message");
    }

    @Test
    public void testMessage() {
        String message = "New test message";
        errorResponse.setMessage(message);
        assertEquals(message, errorResponse.getMessage());
    }

    @Test
    public void testTimestamp() {
        LocalDateTime timestamp = LocalDateTime.now();
        errorResponse.setTimestamp(timestamp);
        assertEquals(timestamp, errorResponse.getTimestamp());
    }
}
