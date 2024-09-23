package org.savingapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.savingapp.dto.ChallengeDTO;
import org.savingapp.enums.Category;
import org.savingapp.enums.LifeSituation;
import org.savingapp.model.Challenge;
import org.savingapp.repository.ChallengeRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

class ChallengeServiceImplTest {

    @Mock
    private ChallengeRepository challengeRepository;

    @InjectMocks
    private ChallengeServiceImpl challengeService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void saveChallengeIsCalledWithCorrectParameters() {
        ChallengeDTO challengeDTO = new ChallengeDTO(1L, "Challenge", "Try to drive less!",
                Category.CLOTHING, 2, 100, 400.00, LifeSituation.STUDENT);

        challengeService.saveChallenge(challengeDTO);

        verify(challengeRepository).save(any(Challenge.class));
    }


}