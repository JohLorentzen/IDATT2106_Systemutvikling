package org.savingapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.savingapp.dto.SavingsGoalDTO;
import org.savingapp.model.SavingsGoal;
import org.savingapp.model.User;
import org.savingapp.repository.SavingsGoalRepository;
import org.savingapp.repository.SavingsPathRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.savingapp.service.SavingsGoalServiceImpl.toEntity;

class SavingsGoalServiceImplTest {

    @Mock
    SavingsGoalRepository savingsGoalRepository;
    @Mock
    SavingsPathRepository savingsPathRepository;
    @InjectMocks
    SavingsGoalServiceImpl savingsGoalService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getActiveGoalWhenGoalExists() {
        User user = new User();
        user.setId(1L);
        SavingsGoal activeSavingsGoal = new SavingsGoal(1L, "Trip To Spain", 10000, 1000, LocalDate.now(), true, user);
        SavingsGoal inactiveSavingsGoal = new SavingsGoal(2L, "Trip To Cabin", 10000, 1000, LocalDate.now(), false, user);
        List<SavingsGoal> savingGoals = new ArrayList<>(Arrays.asList(
                activeSavingsGoal,
                inactiveSavingsGoal
        ));
        user.setSavingGoals(savingGoals);

        SavingsGoalDTO result = savingsGoalService.getActiveSavingsGoal(user);
        assertEquals(result.getId(), activeSavingsGoal.getId());
        assertTrue(result.isActive());
    }

    @Test
    public void getAllCurrentSavingGoals() {
        User user = new User();
        user.setId(1L);
        SavingsGoal goal1 = new SavingsGoal(1L, "Trip To Spain", 10000, 1000, LocalDate.now(), true, user);
        SavingsGoal goal2 = new SavingsGoal(2L, "Trip To Cabin", 10000, 1000, LocalDate.now(), false, user);
        SavingsGoal goal3 = new SavingsGoal(3L, "Trip To Cabin", 10000, 1000, LocalDate.of(2023, 1, 1), false, user);
        List<SavingsGoal> savingGoals = new ArrayList<>(Arrays.asList(
                goal1,
                goal2,
                goal3
        ));
        user.setSavingGoals(savingGoals);

        List<SavingsGoalDTO> result = savingsGoalService.getCurrentSavingGoals(user);
        assertEquals(2, result.size());
    }

    @Test
    public void getAllArchivedSavingGoals() {
        User user = new User();
        user.setId(1L);
        SavingsGoal goal1 = new SavingsGoal(1L, "Trip To Spain", 10000, 1000,
                LocalDate.now(), true, user);
        SavingsGoal goal2 = new SavingsGoal(2L, "Trip To Cabin", 10000, 1000,
                LocalDate.now(), false, user);
        SavingsGoal goal3 = new SavingsGoal(3L, "Trip To Cabin", 10000, 1000,
                LocalDate.of(2023, 1, 1), false, user);
        SavingsGoal goal4 = new SavingsGoal(4L, "Trip To Cabin", 10000, 1000,
                LocalDate.of(2022, 5, 5), false, user);
        List<SavingsGoal> savingGoals = new ArrayList<>(Arrays.asList(
                goal1,
                goal2,
                goal3,
                goal4
        ));
        user.setSavingGoals(savingGoals);

        List<SavingsGoalDTO> result = savingsGoalService.getArchivedSavingGoals(user);
        assertEquals(2, result.size());
    }

    @Test
    public void updateGoalSuccessfullyUpdates() {
        User user = new User();
        user.setId(1L);
        SavingsGoal goal1 = new SavingsGoal(1L, "Trip To Spain", 10000, 1000,
                LocalDate.now(), true, user);
        SavingsGoal goal2 = new SavingsGoal(2L, "Trip To Cabin", 10000, 1000,
                LocalDate.now(), false, user);
        List<SavingsGoal> savingGoals = new ArrayList<>(Arrays.asList(
                goal1,
                goal2
        ));
        user.setSavingGoals(savingGoals);

        SavingsGoalDTO newGoal = SavingsGoalDTO.builder()
                .id(2L)
                .title("Birthday present")
                .savedAmount(200)
                .goalAmount(1000)
                .endDate(LocalDate.now())
                .isActive(false)
                .build();

        when(savingsGoalRepository.findById(newGoal.getId())).thenReturn(Optional.of(toEntity(newGoal, user)));
        savingsGoalService.updateSavingsGoal(newGoal, user);

        assertNotEquals(
                goal2,
                SavingsGoalServiceImpl.unwrapGoal(savingsGoalRepository.findById(2L), 2L)
        );
    }
}