package org.savingapp.service;

import org.savingapp.dto.UserStatsDTO;
import org.savingapp.model.User;


/**
 * Interface for the UserStatsService.
 */
public interface UserStatsService {
    /**
     * Get the user stats for a given user.
     *
     * @param user the user to get the stats for
     * @return the user stats
     */
    UserStatsDTO getUserStats(User user);
}
