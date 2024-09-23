package org.savingapp.service;

import org.savingapp.dto.CompletedUserChallengeDTO;
import org.savingapp.dto.SavingsPathDTO;
import org.savingapp.dto.UserChallengeDTO;
import org.savingapp.model.User;

import java.util.List;


/**
 * Interface for the SavingsPathService.
 */
public interface SavingsPathService {

    /**
     * Fetch a savings path by its ID.
     *
     * @param id The ID.
     * @return The savings path.
     */
    SavingsPathDTO getSavingsPathById(Long id);

    /**
     * Get the user's savings path based on its active savings goal.
     *
     * @param user The logged in user.
     * @return The savings path.
     */
    SavingsPathDTO getSavingsPathByUser(User user);

    /**
     * Add a completed user challenge.
     *
     * @param userChallengeDTO The user challenge DTO.
     * @param user             The user.
     */

    void addCompletedUserChallenge(UserChallengeDTO userChallengeDTO, User user);

    /**
     * Get the completed user challenges for a given user.
     *
     * @param user The user to get the completed user challenges for.
     * @return The completed user challenges.
     */
    List<CompletedUserChallengeDTO> getCompletedUserChallenges(User user);

    /**
     * Get the completed user challenges for a given savings path.
     *
     * @param id   The savings path ID.
     * @param user The user to get the completed user challenges for.
     * @return The completed user challenges.
     */
    List<CompletedUserChallengeDTO> getCompletedUserChallengesBySavingsPathId(Long id, User user);
}
