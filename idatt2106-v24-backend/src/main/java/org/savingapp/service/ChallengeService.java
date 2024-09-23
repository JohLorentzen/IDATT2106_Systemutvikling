package org.savingapp.service;

import org.savingapp.dto.ChallengeDTO;
import org.savingapp.model.Challenge;
import org.savingapp.model.User;

import java.util.Optional;

/**
 * This interface defines the contract for the ChallengeService.
 * The ChallengeService is responsible for managing challenges for the user.
 * This includes retrieving, saving, and generating challenges based on various parameters.
 */
public interface ChallengeService {

    /**
     * Retrieve the current challenge.
     *
     * @return An Optional containing the current ChallengeDTO if it exists, otherwise an empty Optional.
     */
    Optional<ChallengeDTO> getChallenge();

    /**
     * Save a challenge.
     *
     * @param challengeDTO The ChallengeDTO to be saved.
     */
    void saveChallenge(ChallengeDTO challengeDTO);

    /**
     * Generate a random challenge for a user.
     *
     * @param user The user for whom the challenge is generated.
     * @return The generated Challenge.
     */
    Challenge getRandomChallenge(User user);

    /**
     * Generate a challenge based on a specific sum for a user.
     *
     * @param user The user for whom the challenge is generated.
     * @param sum  The sum of the challenge.
     * @return The generated Challenge.
     */
    Challenge getChallengeBySum(User user, int sum);

    /**
     * Generate a challenge based on a specific duration for a user.
     *
     * @param user     The user for whom the challenge is generated.
     * @param duration The duration of the challenge.
     * @return The generated Challenge.
     */
    Challenge getChallengeByDuration(User user, int duration);
}