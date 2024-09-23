package org.savingapp.service;

import lombok.AllArgsConstructor;
import org.savingapp.dto.UserStatsDTO;
import org.savingapp.exception.EntityNotFoundException;
import org.savingapp.model.*;
import org.savingapp.repository.UserStatsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * Implementation of the UserStatsService.
 */
@AllArgsConstructor
@Service
public class UserStatsServiceImpl implements UserStatsService {

    private final UserStatsRepository userStatsRepository;


    /**
     * Fetches the user's stats.
     * @param user The user.
     * @return The stats.
     */
    @Override
    public UserStatsDTO getUserStats(User user) {
        UserStats currentUserStats = user.getUserStats();

        UserStats userStats = new UserStats();
        List<SavingsGoal> savingGoals = user.getSavingGoals();
        double totalAmountSaved = getTotalAmountSaved(savingGoals);
        int completedGoals = getCompletedSavingGoals(savingGoals);
        int completedChallenges = getCompletedChallenges(user.getUserChallenges());

        userStats.setTotalSaved(totalAmountSaved);
        userStats.setCompletedGoals(completedGoals);
        userStats.setCompletedChallenges(completedChallenges);
        userStats.setUser(user);
        if (currentUserStats != null) {
           userStats.setId(currentUserStats.getId());
        }

        userStatsRepository.save(userStats);
        return toDTO(userStats);
    }


    /**
     * Fetches the total amount saved by the user.
     * @param savingGoals The user's saving goals.
     * @return The total amount saved.
     */
    private double getTotalAmountSaved(List<SavingsGoal> savingGoals) {
        if (savingGoals == null || savingGoals.isEmpty()) return 0;

        return savingGoals.stream()
            .mapToDouble(SavingsGoal::getSavedAmount)
            .sum();
    }


    /**
     * Fetches the number of completed challenges by the user.
     * @param userChallenges The user's challenges.
     * @return The number of completed challenges.
     */
    private int getCompletedChallenges(List<UserChallenge> userChallenges) {
        if (userChallenges == null || userChallenges.isEmpty()) return 0;

        return (int) userChallenges.stream()
            .filter(c -> c.getAmountSaved() >= c.getGoalAmount()).count();
    }


    /**
     * Fetches the number of completed saving goals by the user.
     * @param savingGoals The user's saving goals.
     * @return The number of completed saving goals.
     */
    private int getCompletedSavingGoals(List<SavingsGoal> savingGoals) {
        if (savingGoals == null || savingGoals.isEmpty()) return 0;

        return (int) savingGoals.stream()
            .filter(g -> g.getSavedAmount() >= g.getGoalAmount()).count();
    }


    /**
     * Maps a user stats entity to a DTO.
     * @param entity The user stats.
     * @return The DTO.
     */
    public static UserStatsDTO toDTO(UserStats entity) {
        return new UserStatsDTO(
            entity.getId(),
            entity.getTotalSaved(),
            entity.getCompletedChallenges(),
            entity.getCompletedGoals(),
            entity.getUser().getId()
        );
    }


    /**
     * Maps a user stats DTO to an entity.
     * @param unwrappedStats The unwrapped stats/DTO.
     * @return The entity.
     */
    private static UserStats unwrapStats(Optional<UserStats> unwrappedStats) {
        if (unwrappedStats.isPresent()) return unwrappedStats.get();
        else throw new EntityNotFoundException(UserStats.class);
    }
}
