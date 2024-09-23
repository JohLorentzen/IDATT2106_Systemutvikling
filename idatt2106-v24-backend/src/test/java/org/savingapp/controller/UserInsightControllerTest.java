package org.savingapp.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.savingapp.dto.UserInsightDTO;
import org.savingapp.enums.Category;
import org.savingapp.enums.InterestEnum;
import org.savingapp.enums.LifeSituation;
import org.savingapp.enums.SkillLevel;
import org.savingapp.exception.EntityNotFoundException;
import org.savingapp.model.User;
import org.savingapp.model.UserCategoryStats;
import org.savingapp.model.UserInsight;
import org.savingapp.service.UserInsightServiceImpl;
import org.savingapp.util.UserUtil;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class UserInsightControllerTest {
    @Mock
    private UserInsightServiceImpl insightService;

    @Mock
    private UserUtil userUtil;

    @InjectMocks
    private UserInsightController insightController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(insightController).build();
    }

    @Test
    @WithMockUser(username = "user")
    public void getInsightReturnsInsightWhenExists() throws Exception {
        UserCategoryStats userCategoryStats = new UserCategoryStats();
        userCategoryStats.setCategory(Category.HOME);
        userCategoryStats.setEfficiencyScoreOffset(0.03f);
        userCategoryStats.setTrend(3500);

        UserInsightDTO userInsight = new UserInsightDTO(1L, SkillLevel.AVERAGE, LifeSituation.STUDENT,
                List.of(InterestEnum.REAL_ESTATE), List.of(Category.HOME),
                1L);
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setUserInsight(UserInsightServiceImpl.toEntity(userInsight, user));

        when(userUtil.getCurrentUser()).thenReturn(user);
        when(insightService.getUserInsight(user)).thenReturn(userInsight);

        mockMvc.perform(get("/rest/user-insight").with(user("user")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user")
    public void getInsightReturnsNewUserInsightWhenUserHasNoInsights() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setUserInsight(null);

        when(userUtil.getCurrentUser()).thenReturn(user);
        when(insightService.getUserInsight(user)).thenThrow(new EntityNotFoundException(UserInsight.class));

        mockMvc.perform(get("/rest/user-insight").with(user("user")))
                .andExpect(status().isOk());
    }

}