package org.savingapp.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompletedUserChallengeTest {
    private CompletedUserChallenge completedUserChallenge;

    @BeforeEach
    public void setup() {
        completedUserChallenge = new CompletedUserChallenge();
    }

    @Test
    public void testId() {
        Long idValue = 1L;
        completedUserChallenge.setId(idValue);
        assertEquals(idValue, completedUserChallenge.getId());
    }

    @Test
    public void testUserChallenge() {
        UserChallenge userChallenge = new UserChallenge();
        completedUserChallenge.setUserChallenge(userChallenge);
        assertEquals(userChallenge, completedUserChallenge.getUserChallenge());
    }

    @Test
    public void testPointOfCompletion() {
        Double pointOfCompletion = 75.0;
        completedUserChallenge.setPointOfCompletion(pointOfCompletion);
        assertEquals(pointOfCompletion, completedUserChallenge.getPointOfCompletion());
    }

    @Test
    public void testSavingsPath() {
        SavingsPath savingsPath = new SavingsPath();
        completedUserChallenge.setSavingsPath(savingsPath);
        assertEquals(savingsPath, completedUserChallenge.getSavingsPath());
    }
}
