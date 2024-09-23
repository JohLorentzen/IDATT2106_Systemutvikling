package org.savingapp.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.savingapp.dto.SavingsGoalDTO;
import org.savingapp.model.User;
import org.savingapp.service.SavingsGoalServiceImpl;
import org.savingapp.util.UserUtil;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SavingsGoalControllerTest {
    @Mock
    private SavingsGoalServiceImpl goalService;
    @Mock
    private UserUtil userUtil;
    @InjectMocks
    private SavingsGoalController savingsGoalController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(savingsGoalController).build();
    }

    @Test
    @WithMockUser(username = "user")
    public void getActiveSavingsGoal() throws Exception {
        SavingsGoalDTO savingsGoal = SavingsGoalDTO.builder()
                .id(1L)
                .title("Trip To Spain")
                .goalAmount(10000)
                .savedAmount(1000)
                .endDate(LocalDate.now())
                .isActive(true)
                .build();

        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setSavingGoals(List.of(SavingsGoalServiceImpl.toEntity(savingsGoal, user)));

        when(userUtil.getCurrentUser()).thenReturn(user);
        when(goalService.getActiveSavingsGoal(user)).thenReturn(savingsGoal);

        mockMvc.perform(get("/rest/savings-goal").with(user("user")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user")
    public void setNewActiveSavingsGoalShouldReturn200WhenSuccessful() throws Exception {
        Long id = 1L;

        mockMvc.perform(post("/rest/set-active/{id}", id).with(user("user")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user")
    public void getCurrentGoalsShouldReturn200WhenGoalsExist() throws Exception {
        List<SavingsGoalDTO> goals = Arrays.asList(
                SavingsGoalDTO.builder().id(1L).title("Goal 1").build(),
                SavingsGoalDTO.builder().id(2L).title("Goal 2").build()
        );
        when(goalService.getCurrentSavingGoals(userUtil.getCurrentUser())).thenReturn(goals);

        mockMvc.perform(get("/rest/current-saving-goals").with(user("user")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user")
    public void deleteSavingsGoalShouldReturn200WhenSuccessful() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/rest/savings-goal/{id}", id).with(user("user")))
                .andExpect(status().isOk());
    }
}