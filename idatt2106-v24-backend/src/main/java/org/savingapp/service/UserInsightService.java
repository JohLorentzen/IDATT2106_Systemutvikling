package org.savingapp.service;

import org.savingapp.dto.UserInsightDTO;
import org.savingapp.model.User;


/**
 * Interface for the UserInsightService.
 */
public interface UserInsightService {

    /**
     * Get the user insight for a given user.
     * The insight is a collection of information about the user's life situation, interests, skill level and categories.
     *
     * @param user the user to get the insight for
     * @return the user insight
     */
    UserInsightDTO getUserInsight(User user);

    /**
     * Save the user insight for a given user.
     * The insight is a collection of information about the user's life situation, interests, skill level and categories.
     *
     * @param user the user to save the insight for
     * @param insightDTO the insight to save
     * @return the saved user insight
     */
    UserInsightDTO saveInsight(User user, UserInsightDTO insightDTO);
}
