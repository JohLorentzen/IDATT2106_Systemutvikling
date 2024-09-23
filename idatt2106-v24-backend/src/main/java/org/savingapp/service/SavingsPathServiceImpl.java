package org.savingapp.service;

import lombok.AllArgsConstructor;
import org.savingapp.dto.*;
import org.savingapp.exception.EntityNotFoundException;
import org.savingapp.model.*;
import org.savingapp.repository.CompletedUserChallengeRepository;
import org.savingapp.repository.SavingsGoalRepository;
import org.savingapp.repository.SavingsPathRepository;
import org.savingapp.repository.UserChallengeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Implementation of the SavingsPathService.
 */
@AllArgsConstructor
@Service
public class SavingsPathServiceImpl implements SavingsPathService {

    private final SavingsPathRepository savingsPathRepository;
    private final SavingsGoalRepository savingsGoalRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final CompletedUserChallengeRepository completedUserChallengeRepository;
    private final SavingsGoalServiceImpl savingsGoalService;

    /**
     * Fetch a savings path by its ID.
     *
     * @param id The ID.
     * @return The savings path.
     */
    @Override
    public SavingsPathDTO getSavingsPathById(Long id) {
        Optional<SavingsPath> savingsPath = savingsPathRepository.findById(id);

        return savingsPath.map(path -> SavingsPathDTO.builder()
                .savingsGoal(SavingsGoalServiceImpl.toDTO(path.getSavingsGoal()))
                .build()).orElse(null);
    }


    /**
     * Get the user's savings path based on its active savings goal.
     *
     * @param user The logged in user.
     * @return The savings path.
     */
    @Override
    public SavingsPathDTO getSavingsPathByUser(User user) {
        SavingsGoalDTO activeGoalDTO = savingsGoalService.getActiveSavingsGoal(user);
        if (activeGoalDTO == null) {
            return null;
        }

        SavingsGoal activeGoal = SavingsGoalServiceImpl.toEntity(activeGoalDTO, user);
        SavingsPath savingsPath = savingsPathRepository
                .findBySavingsGoal(activeGoal)
                .orElseThrow(() -> new EntityNotFoundException(user.getId(), SavingsPath.class));

        return new SavingsPathDTO(activeGoalDTO, savingsPath.getCompletedChallenges().stream()
                .map(challenge -> new CompletedUserChallengeDTO(challenge.getId(),
                        toDTO(challenge.getUserChallenge()), challenge.getPointOfCompletion())).toList());
    }


    /**
     * Add a user challenge and save it as a completed user challenge linked to a savings path.
     *
     * @param userChallengeDTO The user challenge to be saved as completed.
     * @param user             The logged in user.
     */
    @Override
    public void addCompletedUserChallenge(UserChallengeDTO userChallengeDTO, User user) {
        Optional<UserChallenge> userChallengeOpt = userChallengeRepository.findById(userChallengeDTO.getId());

        if (userChallengeOpt.isPresent()) {
            UserChallenge userChallenge = userChallengeOpt.get();

            SavingsGoal savingsGoal = SavingsGoalServiceImpl.toEntity(
                    savingsGoalService.getActiveSavingsGoal(user),
                    user
            );

            Double pointOfCompletion = savingsGoal.getSavedAmount() + userChallengeDTO.getSavedAmount();

            Optional<SavingsPath> savingsPathOpt = savingsPathRepository.findBySavingsGoal(savingsGoal);

            if (savingsPathOpt.isPresent()) {
                SavingsPath savingsPath = savingsPathOpt.get();

                CompletedUserChallenge completedUserChallenge = CompletedUserChallenge.builder()
                        .savingsPath(savingsPath)
                        .userChallenge(userChallenge)
                        .pointOfCompletion(pointOfCompletion)
                        .build();

                // Save the completed user challenge
                completedUserChallengeRepository.save(completedUserChallenge);

                // Add the completed user challenge's saved amount to the savings goal total amount saved
                savingsGoal.setSavedAmount(savingsGoal.getSavedAmount() + userChallengeDTO.getSavedAmount());
                savingsGoalRepository.save(savingsGoal);
            }
        } else {
            throw new NullPointerException();
        }
    }


    /**
     * Get all completed user challenges linked to the user's savings path based on the active savings goal.
     *
     * @param user The logged in user.
     * @return A list of the savings path's completed user challenges.
     */
    @Override
    public List<CompletedUserChallengeDTO> getCompletedUserChallenges(User user) {
        SavingsGoal savingsGoal = SavingsGoalServiceImpl.toEntity(
                savingsGoalService.getActiveSavingsGoal(user),
                user
        );

        Optional<SavingsPath> savingsPath = savingsPathRepository.findBySavingsGoal(savingsGoal);

        return getCompletedUserChallengeDTOS(savingsPath);
    }

    public List<CompletedUserChallengeDTO> getCompletedUserChallengesBySavingsPathId(Long id, User user) {
        Optional<SavingsGoal> savingsGoal = savingsGoalRepository.findById(id);


        if (savingsGoal.isPresent()) {
            Optional<SavingsPath> savingsPath = savingsPathRepository.findBySavingsGoal(savingsGoal.get());

            return getCompletedUserChallengeDTOS(savingsPath);
        } else {
            throw new NullPointerException();
        }
    }


    /**
     * Unwrap Optional reads from the completed user challenge repository.
     *
     * @param savingsPath The optional savings path to retrieve completed user challenges from.
     * @return A list of completed user challenge DTOs.
     */
    private List<CompletedUserChallengeDTO> getCompletedUserChallengeDTOS(Optional<SavingsPath> savingsPath) {
        if (savingsPath.isPresent()) {
            List<CompletedUserChallenge> completedUserChallenges =
                    completedUserChallengeRepository.findBySavingsPath(savingsPath.get());

            List<CompletedUserChallengeDTO> completedUserChallengesDTO;
            completedUserChallengesDTO = new ArrayList<>();
            for (CompletedUserChallenge challenge : completedUserChallenges) {
                completedUserChallengesDTO.add(
                        CompletedUserChallengeDTO.builder()
                                .id(challenge.getId())
                                .userChallenge(toDTO(challenge.getUserChallenge()))
                                .pointOfCompletion(challenge.getPointOfCompletion())
                                .build());
            }

            return completedUserChallengesDTO;
        } else {
            throw new NullPointerException();
        }
    }

    /**
     * Convert a user challenge entity to a DTO.
     *
     * @param userChallenge The user challenge entity.
     * @return The user challenge DTO.
     */
    private UserChallengeDTO toDTO(UserChallenge userChallenge) {
        return UserChallengeDTO.builder()
                .goalAmount(userChallenge.getGoalAmount())
                .savedAmount(userChallenge.getAmountSaved())
                .fromDate(userChallenge.getFromDate())
                .toDate(userChallenge.getToDate())
                .challenge(toDTO(userChallenge.getChallenge()))
                .build();
    }

    /**
     * Convert a challenge entity to a DTO.
     *
     * @param challenge The challenge entity.
     * @return The challenge DTO.
     */
    private ChallengeDTO toDTO(Challenge challenge) {
        return ChallengeDTO.builder()
                .id(challenge.getId())
                .sum(challenge.getSum())
                .averageAmount(challenge.getAverageAmount())
                .name(challenge.getName())
                .category(challenge.getCategory())
                .lifeSituation(challenge.getLifeSituation())
                .description(challenge.getDescription())
                .build();
    }
}
