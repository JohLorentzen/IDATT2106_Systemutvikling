package org.savingapp.service;

import lombok.AllArgsConstructor;
import org.savingapp.dto.ChallengeDTO;
import org.savingapp.dto.UserChallengeDTO;
import org.savingapp.model.*;
import org.savingapp.repository.ChallengeRepository;
import org.savingapp.repository.CompletedUserChallengeRepository;
import org.savingapp.repository.UserChallengeRepository;
import org.savingapp.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


/**
 * Implementation of the UserChallengeService.
 */
@AllArgsConstructor
@Service
public class UserChallengeServiceImpl implements UserChallengeService {

    private final UserChallengeRepository userChallengeRepository;
    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;
    private final ChallengeService challengeService;
    private final UserCategoryStatsService userCategoryStatsService;
    private final SavingsPathService savingsPathService;
    private final CompletedUserChallengeRepository completedUserChallengeRepository;


    /**
     * Fetches a page of user challenges for a user.
     * @param user The logged in user.
     * @return Page of the challenges.
     */
    @Override
    public Page<UserChallengeDTO> getUserChallenges(Pageable pageable, User user) {
        return userChallengeRepository
                .findByUser(pageable, user)
                .map(userChallenge ->
                        UserChallengeDTO.builder()
                                .id(userChallenge.getId())
                                .goalAmount(userChallenge.getGoalAmount())
                                .savedAmount(userChallenge.getAmountSaved())
                                .fromDate(userChallenge.getFromDate())
                                .toDate(userChallenge.getToDate())
                                .challenge(ChallengeDTO.builder()
                                        .id(userChallenge.getChallenge().getId())
                                        .name(userChallenge.getChallenge().getName())
                                        .description(userChallenge.getChallenge().getDescription())
                                        .category(userChallenge.getChallenge().getCategory())
                                        .duration(userChallenge.getChallenge().getDuration())
                                        .sum(userChallenge.getChallenge().getSum())
                                        .averageAmount(userChallenge.getChallenge().getAverageAmount())
                                        .lifeSituation(userChallenge.getChallenge().getLifeSituation())
                                        .build())
                                .build()
                        );
    }

    /**
     * Maps a challenge entity to a DTO.
     * @param challenge The challenge.
     * @return The DTO.
     */
    private ChallengeDTO mapChallenge(Challenge challenge) {
        return new ChallengeDTO(challenge.getId(), challenge.getName(),
                challenge.getDescription(), challenge.getCategory(),
                challenge.getDuration(), challenge.getSum(),
                challenge.getAverageAmount(), challenge.getLifeSituation());

    }


    /**
     * Finishes a challenge and checks the saved amount versus the goal amount.
     *
     * @param userChallenge The challenge to finish.
     */
    @Override
    public void finishChallenge(UserChallenge userChallenge) {
        if (userChallenge.getAmountSaved() >= userChallenge.getGoalAmount()) {
            winChallenge(userChallenge);
            savingsPathService.addCompletedUserChallenge(UserChallengeDTO.builder()
                            .id(userChallenge.getId())
                            .goalAmount(userChallenge.getGoalAmount())
                            .savedAmount(userChallenge.getAmountSaved())
                            .fromDate(userChallenge.getFromDate())
                            .toDate(userChallenge.getToDate())
                            .challenge(mapChallenge(userChallenge.getChallenge()))
                            .build(),
                    userChallenge.getUser());
        } else if (LocalDate.now().isAfter(userChallenge.getToDate())) {
            loseChallenge(userChallenge);
            savingsPathService.addCompletedUserChallenge(UserChallengeDTO.builder()
                            .id(userChallenge.getId())
                            .goalAmount(userChallenge.getGoalAmount())
                            .savedAmount(userChallenge.getAmountSaved())
                            .fromDate(userChallenge.getFromDate())
                            .toDate(userChallenge.getToDate())
                            .challenge(mapChallenge(userChallenge.getChallenge()))
                            .build(),
                    userChallenge.getUser());
        }
    }

    /**
     * Finishes a challenge and raises the difficulty score.
     *
     * @param userChallenge The challenge to finish.
     */
     void winChallenge(UserChallenge userChallenge) {
        User user = userChallenge.getUser();

        float currentOffset = userCategoryStatsService
                .getCategoryStats(user, userChallenge.getChallenge().getCategory())
                .getEfficiencyScoreOffset();
        userCategoryStatsService.updateEfficiency(user,
                userChallenge.getChallenge().getCategory(),
                currentOffset + 0.01f);

        float currentRelevance = userCategoryStatsService
                .getCategoryStats(user, userChallenge.getChallenge().getCategory())
                .getRelevanceScore();
        userCategoryStatsService.updateRelevance(user,
                userChallenge.getChallenge().getCategory(),
                currentRelevance + 2.0f);

        user.getUserChallenges().remove(userChallenge);
        userRepository.save(user);
    }

    /**
     * Finishes a challenge and lowers the difficulty score.
     *
     * @param userChallenge The challenge to finish.
     */
    private void loseChallenge(UserChallenge userChallenge) {
        User user = userChallenge.getUser();
        float currentOffset = userCategoryStatsService
                .getCategoryStats(user, userChallenge.getChallenge().getCategory())
                .getEfficiencyScoreOffset();

        float currentRelevance = userCategoryStatsService
                .getCategoryStats(user, userChallenge.getChallenge().getCategory())
                .getRelevanceScore();

        userCategoryStatsService.updateRelevance(user, userChallenge.getChallenge().getCategory(),currentRelevance + 1.9f);
        userCategoryStatsService.updateEfficiency(user, userChallenge.getChallenge().getCategory(),currentOffset - 0.01f);
        user.getUserChallenges().remove(userChallenge);
        userRepository.save(user);
    }

    /**
     * Accepts a challenge and raises the relevance score.
     *
     * @param userChallenge The challenge to accept.
     */
    @Override
    public void acceptChallenge(UserChallenge userChallenge) {
        User user = userChallenge.getUser();

        float currentRelevance = userCategoryStatsService
                .getCategoryStats(user, userChallenge.getChallenge().getCategory())
                .getRelevanceScore();

        userCategoryStatsService.updateRelevance(user, userChallenge.getChallenge().getCategory(),currentRelevance - 1.9f);
        user.getUserChallenges().add(userChallenge);
        userRepository.save(user);
    }

    /**
     * Rejects a challenge and lowers the relevance score.
     *
     * @param challenge The challenge to reject.
     */
    @Override
    public void rejectChallenge(User user, Challenge challenge) {
        float currentRelevance = userCategoryStatsService
                .getCategoryStats(user, challenge.getCategory())
                .getRelevanceScore();

        userCategoryStatsService.updateRelevance(user, challenge.getCategory(),currentRelevance - 0.1f);
        userRepository.save(user);
    }

    @Override
    public void calculateAmountSaved(User user) {
        System.out.println("Calculating amount saved for user: " + user.getUsername());
        List<UserChallenge> userChallenges = userChallengeRepository.findByUserId(user.getId()).stream()
                .filter(userChallenge -> userChallenge.getToDate().isAfter(LocalDate.now()))
                .toList();

        user.getAccounts().stream()
                .flatMap(account -> account.getOutgoingTransactions().stream())
                .forEach(transaction -> userChallenges.stream()
                        .filter(userChallenge -> transaction.getCategory() == userChallenge.getChallenge().getCategory() && completedUserChallengeRepository.findByUserChallengeId(userChallenge.getId()).isEmpty())
                        .forEach(userChallenge -> {
                            double averageTrend = userCategoryStatsService.getCategoryStats(user, userChallenge.getChallenge().getCategory()).getTrend() / 30;
                            int timeSoFar = LocalDate.now().getDayOfYear() - userChallenge.getFromDate().getDayOfYear();
                            double amountSaved = averageTrend * timeSoFar;

                            userChallenge.setAmountSaved(userChallenge.getAmountSaved() + amountSaved);
                            userChallengeRepository.save(userChallenge);
                            finishChallenge(userChallenge);
                            System.out.println("Amount saved for user: " + user.getUsername() + " is: " + amountSaved);
                        })
                );
    }



    /**
     * Saves a user challenge.
     *
     * @param userChallenge The challenge to save.
     * @param user The user to save the challenge for.
     */
    @Override
    public void saveUserChallenge(UserChallengeDTO userChallenge, User user) {
        userChallengeRepository.save(new UserChallenge(userChallenge.getId(), user, userChallenge.getGoalAmount(),
                userChallenge.getSavedAmount(), userChallenge.getFromDate(), userChallenge.getToDate(),
                challengeRepository.findById(userChallenge.getChallenge().getId()).orElseThrow()));
    }


    /**
     * Creates a random challenge for a user.
     *
     * @param user The user to create the challenge for.
     * @return The created challenge.
     */
    @Override
    public UserChallenge createRandomUserChallenge(User user) {
        Challenge challenge = challengeService.getRandomChallenge(user);
        return new UserChallenge(null, user, (double) challenge.getSum(), 0.0,
                LocalDate.now(), LocalDate.now().plusDays(challenge.getDuration()), challenge);
    }

    /**
     * Creates a challenge for a user based on the sum.
     *
     * @param user The user to create the challenge for.
     * @param sum The sum the challenge will have.
     * @return The created challenge.
     */
    @Override
    public UserChallenge createUserChallengeBySum(User user, int sum) {
        Challenge challenge = challengeService.getChallengeBySum(user, sum);
        return new UserChallenge(null, user, (double) challenge.getSum(), 0.0,
                LocalDate.now(), LocalDate.now().plusDays(challenge.getDuration()), challenge);
    }

    /**
     * Creates a challenge for a user based on the duration.
     *
     * @param user The user to create the challenge for.
     * @param duration The duration the challenge will have.
     * @return The created challenge.
     */
    @Override
    public UserChallenge createUserChallengeByDuration(User user, int duration) {
        Challenge challenge = challengeService.getChallengeByDuration(user, duration);
        return new UserChallenge(null, user, (double) challenge.getSum(), 0.0,
                LocalDate.now(), LocalDate.now().plusDays(challenge.getDuration()), challenge);
    }
}
