package org.savingapp.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SavingsPathDTOTest {
    private SavingsPathDTO savingsPathDTO;

    @BeforeEach
    public void setup() {
        savingsPathDTO = new SavingsPathDTO();
    }

    @Test
    public void testSavingsGoal() {
        SavingsGoalDTO savingsGoal = new SavingsGoalDTO();
        // Set properties for savingsGoal here
        savingsPathDTO.setSavingsGoal(savingsGoal);
        assertEquals(savingsGoal, savingsPathDTO.getSavingsGoal());
    }

    @Test
    public void testCompletedChallenges() {
        CompletedUserChallengeDTO completedUserChallengeDTO = new CompletedUserChallengeDTO();
        // Set properties for completedUserChallengeDTO here
        savingsPathDTO.setCompletedChallenges(Collections.singletonList(completedUserChallengeDTO));
        assertEquals(1, savingsPathDTO.getCompletedChallenges().size());
    }
}
