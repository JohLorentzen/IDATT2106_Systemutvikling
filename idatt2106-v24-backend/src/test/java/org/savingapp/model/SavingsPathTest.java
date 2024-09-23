package org.savingapp.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SavingsPathTest {
    private SavingsPath savingsPath;

    @BeforeEach
    public void setup() {
        savingsPath = new SavingsPath();
    }

    @Test
    public void testId() {
        Long idValue = 1L;
        savingsPath.setId(idValue);
        assertEquals(idValue, savingsPath.getId());
    }

    @Test
    public void testSavingsGoal() {
        SavingsGoal savingsGoal = new SavingsGoal();
        savingsPath.setSavingsGoal(savingsGoal);
        assertEquals(savingsGoal, savingsPath.getSavingsGoal());
    }

    @Test
    public void testCompletedChallenges() {
        List<CompletedUserChallenge> completedChallenges = new ArrayList<>();
        CompletedUserChallenge challenge = new CompletedUserChallenge();
        completedChallenges.add(challenge);
        savingsPath.setCompletedChallenges(completedChallenges);
        assertEquals(completedChallenges, savingsPath.getCompletedChallenges());
    }
}
