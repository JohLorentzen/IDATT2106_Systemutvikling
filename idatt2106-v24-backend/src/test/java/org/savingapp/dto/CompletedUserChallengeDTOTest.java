package org.savingapp.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompletedUserChallengeDTOTest {
    private CompletedUserChallengeDTO completedUserChallengeDTO;

    @BeforeEach
    public void setup() {
        completedUserChallengeDTO = new CompletedUserChallengeDTO();
    }

    @Test
    public void testId() {
        Long id = 1L;
        completedUserChallengeDTO.setId(id);
        assertEquals(id, completedUserChallengeDTO.getId());
    }

    @Test
    public void testUserChallenge() {
        UserChallengeDTO userChallengeDTO = new UserChallengeDTO();
        // Set properties for userChallengeDTO here
        completedUserChallengeDTO.setUserChallenge(userChallengeDTO);
        assertEquals(userChallengeDTO, completedUserChallengeDTO.getUserChallenge());
    }

    @Test
    public void testPointOfCompletion() {
        Double pointOfCompletion = 100.0;
        completedUserChallengeDTO.setPointOfCompletion(pointOfCompletion);
        assertEquals(pointOfCompletion, completedUserChallengeDTO.getPointOfCompletion());
    }
}
