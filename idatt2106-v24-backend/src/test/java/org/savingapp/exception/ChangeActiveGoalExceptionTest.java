package org.savingapp.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChangeActiveGoalExceptionTest {
    @Test
    public void testExceptionMessage() {
        Long idValue = 1L;
        ChangeActiveGoalException exception = new ChangeActiveGoalException(idValue);
        assertEquals("The goal with id " + idValue + " is already active or end date has already passed.", exception.getMessage());
    }
}
