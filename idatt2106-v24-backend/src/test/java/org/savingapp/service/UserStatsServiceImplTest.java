package org.savingapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.savingapp.model.SavingsGoal;
import org.savingapp.model.User;
import org.savingapp.model.UserStats;
import org.savingapp.repository.UserRepository;
import org.savingapp.repository.UserStatsRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserStatsServiceImplTest {
    @Mock
    UserStatsRepository userStatsRepository;

    @Mock
    UserRepository userRepo;

    @InjectMocks
    UserStatsServiceImpl userStatsService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void successfullyRetrievesUserStatsObject() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        UserStats userStats = new UserStats(1L, 0, 0, 0, user);
        user.setUserStats(userStats); // user has no stats currently

        assertEquals(UserStatsServiceImpl.toDTO(
                        userStats),
                userStatsService.getUserStats(user)
        );
    }

    @Test
    void successfullyCalculatesTotalSavedAndCompletedAmount() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        UserStats userStats = new UserStats(1L, 200, 0, 2, user);
        user.setUserStats(userStats);

        List<SavingsGoal> savingsGoals = new ArrayList<>(
                Arrays.asList(
                        new SavingsGoal(1L, "Trip", 100, 100,
                                LocalDate.now(), false, user),
                        new SavingsGoal(2L, "Different trip", 100, 100,
                                LocalDate.now(), false, user)
                )
        );
        user.setSavingGoals(savingsGoals);

        assertEquals(
                UserStatsServiceImpl.toDTO(userStats),
                userStatsService.getUserStats(user)
        );
    }
}