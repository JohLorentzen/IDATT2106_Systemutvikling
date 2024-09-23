package org.savingapp.service;

import org.savingapp.enums.Category;
import org.savingapp.model.User;
import org.savingapp.model.UserCategoryStats;

import java.time.LocalDateTime;


/**
 * Interface for the UserCategoryStatsService.
 */
public interface UserCategoryStatsService {
    void initUserCategoryStats(User user);
    boolean isTimestampInPreviousMonth(LocalDateTime timestamp);
    double calculateCategoryTrend(User user, Category category);
    void updateCategoryStats(User user, Category category, int trend, float relevanceScore, float efficiencyScoreOffset);
    void updateRelevance(User user, Category category, float relevanceScore);
    void updateEfficiency(User user, Category category, float efficiencyScoreOffset);
    void updateTrend(User user, Category category, int trend);
    UserCategoryStats getCategoryStats(User user, Category category);
}
