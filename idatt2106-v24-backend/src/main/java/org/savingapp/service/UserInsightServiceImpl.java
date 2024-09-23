package org.savingapp.service;

import lombok.AllArgsConstructor;
import org.savingapp.dto.UserInsightDTO;
import org.savingapp.exception.EntityNotFoundException;
import org.savingapp.model.User;
import org.savingapp.model.UserInsight;
import org.savingapp.repository.UserInsightRepository;
import org.springframework.stereotype.Service;


/**
 * Implementation of the UserInsightService.
 */
@AllArgsConstructor
@Service
public class UserInsightServiceImpl implements UserInsightService {

    private final UserInsightRepository insightRepo;

    /**
     * Maps a user insight entity to a DTO.
     *
     * @param userInsight The user insight.
     * @return The DTO.
     */
    public static UserInsightDTO toDTO(UserInsight userInsight) {
        return new UserInsightDTO(
                userInsight.getId(),
                userInsight.getSkillLevel(),
                userInsight.getLifeSituation(),
                userInsight.getInterests(),
                userInsight.getCategories(),
                userInsight.getUser().getId()
        );
    }

    /**
     * Maps a user insight DTO to an entity.
     *
     * @param userInsightDTO The DTO.
     * @param user           The user.
     * @return The entity.
     */
    public static UserInsight toEntity(UserInsightDTO userInsightDTO, User user) {
        return new UserInsight(
                userInsightDTO.getId(),
                userInsightDTO.getSkillLevel(),
                userInsightDTO.getLifeSituation(),
                userInsightDTO.getInterests(),
                userInsightDTO.getCategories(),
                user
        );
    }

    /**
     * Get the user insight for a given user.
     * The insight is a collection of information about the user's life situation, interests, skill level and categories.
     *
     * @param user the user to get the insight for
     * @return the user insight
     */
    @Override
    public UserInsightDTO getUserInsight(User user) {
        UserInsight userInsight = user.getUserInsight();
        if (userInsight == null) throw new EntityNotFoundException(UserInsight.class);

        return toDTO(userInsight);
    }

    /**
     * Saves the user's insight.
     *
     * @param user       The user.
     * @param insightDTO The insight.
     * @return The saved insight.
     */
    @Override
    public UserInsightDTO saveInsight(User user, UserInsightDTO insightDTO) {
        UserInsight userInsight = user.getUserInsight();

        if (userInsight == null) {
            userInsight = toEntity(insightDTO, user);
        } else {
            userInsight.setLifeSituation(insightDTO.getLifeSituation());
            userInsight.setCategories(insightDTO.getCategories());
            userInsight.setInterests(insightDTO.getInterests());
            userInsight.setSkillLevel(insightDTO.getSkillLevel());
            userInsight = toEntity(insightDTO, user);
        }
        insightRepo.save(userInsight);
        return toDTO(userInsight);
    }
}
