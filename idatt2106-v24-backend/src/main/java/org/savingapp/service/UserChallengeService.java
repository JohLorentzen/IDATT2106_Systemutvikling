package org.savingapp.service;

import org.savingapp.dto.UserChallengeDTO;
import org.savingapp.model.Challenge;
import org.savingapp.model.User;
import org.savingapp.model.UserChallenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * Interface for the UserChallengeService.
 */
public interface UserChallengeService {

    /**
     * Get the user challenges for a given user.
     *
     * @param pageable the page request
     * @param user     the user to get the challenges for
     * @return the user challenges
     */
    Page<UserChallengeDTO> getUserChallenges(Pageable pageable, User user);

    /**
     * Finishes a challenge and checks the saved amount versus the goal amount.
     *
     * @param userChallenge The challenge to finish.
     */
    void finishChallenge(UserChallenge userChallenge);

    /**
     * Accept a user challenge.
     * This will mark the challenge as accepted.
     *
     * @param userChallenge the user challenge to accept
     */
    void acceptChallenge(UserChallenge userChallenge);

    /**
     * Reject a user challenge.
     * This will mark the challenge as rejected.
     *
     * @param user      the user to reject the challenge for
     * @param challenge the challenge to reject
     */
    void rejectChallenge(User user, Challenge challenge);

    /**
     * Calculate the amount saved for a user.
     *
     * @param user the user to calculate the amount saved for
     */
    void calculateAmountSaved(User user);

    /**
     * Save a user challenge.
     *
     * @param userChallenge the user challenge to save
     * @param user          the user to save the challenge for
     */
    void saveUserChallenge(UserChallengeDTO userChallenge, User user);

    /**
     * Create a random user challenge.
     *
     * @param user the user to create the challenge for
     * @return the created challenge
     */
    UserChallenge createRandomUserChallenge(User user);

    /**
     * Create a user challenge by sum.
     *
     * @param user the user to create the challenge for
     * @param sum  the sum to save
     * @return the created challenge
     */
    UserChallenge createUserChallengeBySum(User user, int sum);

    /**
     * Create a user challenge by duration.
     *
     * @param user     the user to create the challenge for
     * @param duration the duration of the challenge
     * @return the created challenge
     */
    UserChallenge createUserChallengeByDuration(User user, int duration);
}
