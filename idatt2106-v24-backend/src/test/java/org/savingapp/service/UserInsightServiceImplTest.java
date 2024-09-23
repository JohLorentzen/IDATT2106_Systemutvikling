package org.savingapp.service;

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
import org.savingapp.repository.UserInsightRepository;
import org.savingapp.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class UserInsightServiceImplTest {
    @Mock
    UserInsightRepository insightRepo;

    @Mock
    UserRepository userRepo;

    @InjectMocks
    UserInsightServiceImpl insightService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getUserInsightReturnsExistingInsight() {
        UserInsightDTO userInsight = new UserInsightDTO(1L, SkillLevel.AVERAGE, LifeSituation.STUDENT,
                List.of(InterestEnum.REAL_ESTATE), List.of(Category.HOME),
                1L);
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setUserInsight(UserInsightServiceImpl.toEntity(userInsight, user));

        when(userRepo.findByUsername("test")).thenReturn(Optional.of(user));
        when(insightRepo.findByUser(user)).thenReturn(Optional.of(userInsight));

        UserInsightDTO userInsightDTO = insightService.getUserInsight(user);

        assertEquals(userInsight, userInsightDTO);
    }

    @Test
    public void shouldThrowExceptionWhenNoUserInsightIsAvailable() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");

        assertThrows(EntityNotFoundException.class, () -> insightService.getUserInsight(user));
    }
}