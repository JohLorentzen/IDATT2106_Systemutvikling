package org.savingapp.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.savingapp.enums.Category;
import org.savingapp.model.*;
import org.savingapp.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;


/**
 * Implementation of the UserCategoryStatsService.
 */
@Service
@AllArgsConstructor
public class UserCategoryStatsServiceImpl implements UserCategoryStatsService {

    private final UserRepository userRepository;


    /**
     * Initializes the category statistics for a given user
     * @param user the user to initialize the category statistics for
     */
    @Override
    @Transactional
    public void initUserCategoryStats(User user) {
        UserInsight userInsight = user.getUserInsight();
        List<UserCategoryStats> userCategoryStatList = new ArrayList<>();
        if (userInsight == null) {
            userInsight = new UserInsight();
        }
        for (Category category : Category.values()) {
            UserCategoryStats userCategoryStats = new UserCategoryStats();
            userCategoryStats.setCategory(category);
            userCategoryStats.setTrend(calculateCategoryTrend(user, category));
            //TODO: Take a look at pushing down irrelevant categories
            if (userCategoryStats.getTrend() > 0) {
                userCategoryStats.setRelevanceScore(0.5f);
            } else {
                userCategoryStats.setRelevanceScore(0.1f);
            }
            userCategoryStats.setEfficiencyScoreOffset(0f);
            userCategoryStats.setUserInsight(userInsight);
            userCategoryStatList.add(userCategoryStats);
        }
        userInsight.setCategoryStats(userCategoryStatList);
        userInsight.setUser(user);
        userRepository.save(user);
    }

    /**
        * Checks if a timestamp is in the previous month
        * @param timestamp the timestamp to check
        * @return true if the timestamp is in the previous month, false otherwise
     */
    @Override
    public boolean isTimestampInPreviousMonth(LocalDateTime timestamp) {
        LocalDate timestampDate = timestamp.toLocalDate();
        LocalDate firstDayOfPreviousMonth = LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfPreviousMonth = LocalDate.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());

        return (timestampDate.isEqual(firstDayOfPreviousMonth) || timestampDate.isAfter(firstDayOfPreviousMonth))
                && (timestampDate.isEqual(lastDayOfPreviousMonth) || timestampDate.isBefore(lastDayOfPreviousMonth));
    }


    /**
        * Calculates the sum of transactions for a given category in the previous month
        * @param user the user to calculate the category trend for
        * @param category the category to calculate the trend for
        * @return the sum of transactions for the given category in the previous month
     */
    @Override
    @Transactional
    public double calculateCategoryTrend(User user, Category category) {

        double categorySum = 0;
        for (Account accounts : user.getAccounts()) {

            for (Transaction transaction : accounts.getOutgoingTransactions()) {

                if (transaction.getCategory().equals(category)
                        && isTimestampInPreviousMonth(transaction.getTimestamp())) {
                    categorySum += transaction.getAmount();
                }
            }
        }
        return categorySum;
    }

    /**
        * Updates the category statistics for a given user
        * @param user the user to update the category statistics for
        * @param category the category to update the statistics for
        * @param trend the trend to set
        * @param relevanceScore the relevance score to set
        * @param efficiencyScoreOffset the efficiency score offset to set
     */
    @Override
    public void updateCategoryStats(User user, Category category, int trend, float relevanceScore, float efficiencyScoreOffset) {
        UserInsight userInsight = user.getUserInsight();
        if (userInsight == null) {
            initUserCategoryStats(user);
        }

        for (UserCategoryStats userCategoryStats : userInsight.getCategoryStats()) {
            if (userCategoryStats.getCategory().equals(category)) {
                userCategoryStats.setTrend(trend);
                userCategoryStats.setRelevanceScore(relevanceScore);
                userCategoryStats.setEfficiencyScoreOffset(efficiencyScoreOffset);
            }
        }
        userRepository.save(user);
    }

    /**
        * Updates the relevance score for a given user
        * @param user the user to update the relevance score for
        * @param category the category to update the relevance score for
        * @param relevanceScore the relevance score to set
     */
    @Override
    public void updateRelevance(User user, Category category, float relevanceScore) {
        if (relevanceScore > 1) {
            relevanceScore = 1;
        }
        if (relevanceScore < 0 && relevanceScore >= -0.8) {
            relevanceScore = 0;
        }

        UserInsight userInsight = user.getUserInsight();
        if (userInsight == null) {
            initUserCategoryStats(user);
        }
        for (UserCategoryStats userCategoryStats : userInsight.getCategoryStats()) {
            if (userCategoryStats.getCategory().equals(category)) {
                userCategoryStats.setRelevanceScore(relevanceScore);
            }
        }
        userRepository.save(user);
    }

    /**
        * Updates the efficiency score for a given user
        * @param user the user to update the efficiency score for
        * @param category the category to update the efficiency score for
        * @param efficiencyScoreOffset the efficiency score offset to set
     */
    @Override
    public void updateEfficiency(User user, Category category, float efficiencyScoreOffset) {
        UserInsight userInsight = user.getUserInsight();
        if (userInsight == null) {
            initUserCategoryStats(user);
        }
        for (UserCategoryStats userCategoryStats : userInsight.getCategoryStats()) {
            if (userCategoryStats.getCategory().equals(category)) {
                userCategoryStats.setEfficiencyScoreOffset(efficiencyScoreOffset);
            }
        }
        userRepository.save(user);
    }

    /**
     * Updates the trend for a given user
     * @param user the user to update the trend for
     * @param category the category to update the trend for
     * @param trend the trend to set
     */
    @Override
    public void updateTrend(User user, Category category, int trend) {
        UserInsight userInsight = user.getUserInsight();
        if (userInsight == null) {
            initUserCategoryStats(user);
        }
        for (UserCategoryStats userCategoryStats : userInsight.getCategoryStats()) {
            if (userCategoryStats.getCategory().equals(category)) {
                userCategoryStats.setTrend(trend);
            }
        }
        userRepository.save(user);
    }

    /**
     * Gets the category statistics for a given user
     * @param user the user to get the category statistics for
     * @param category the category to get the statistics for
     * @return the category statistics for the given user
     */
    @Override
    public UserCategoryStats getCategoryStats(User user, Category category) {
        UserInsight userInsight = user.getUserInsight();
        for (UserCategoryStats userCategoryStats : userInsight.getCategoryStats()) {
            if (userCategoryStats.getCategory().equals(category)) {
                return userCategoryStats;
            }
        }
        return null;
    }

}
