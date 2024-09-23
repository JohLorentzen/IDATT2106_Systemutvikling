package org.savingapp.service;

import org.savingapp.enums.Category;
import org.savingapp.model.ChallengeSkeleton;
import org.savingapp.model.User;
import org.savingapp.model.UserCategoryStats;

import java.util.List;

/**
 * This interface defines the contract for generating challenge skeletons for the user.
 * A challenge skeleton is a structure that outlines the challenge without the specific details.
 * The details are filled in later based on the user's preferences and past behavior.
 */
public interface GenerateChallengeService {

    /**
     * Calculate the daily rate for a user in a specific category.
     *
     * @param user     The user for whom the daily rate is calculated.
     * @param category The category in which the daily rate is calculated.
     * @return The calculated daily rate.
     */
    double calculateDailyRate(User user, Category category);

    /**
     * Generate a challenge skeleton based on a specific duration.
     *
     * @param user     The user for whom the challenge is generated.
     * @param category The category of the challenge.
     * @param duration The duration of the challenge.
     * @return The generated challenge skeleton.
     */
    ChallengeSkeleton generateSkeletonFromDuration(User user, Category category, int duration);

    /**
     * Generate a challenge skeleton based on a specific sum.
     *
     * @param user     The user for whom the challenge is generated.
     * @param category The category of the challenge.
     * @param sum      The sum of the challenge.
     * @return The generated challenge skeleton.
     */
    ChallengeSkeleton generateChallengeSkeletonFromSum(User user, Category category, int sum);

    /**
     * Generate a random challenge skeleton for a user in a specific category.
     *
     * @param user     The user for whom the challenge is generated.
     * @param category The category of the challenge.
     * @return The generated challenge skeleton.
     */
    ChallengeSkeleton generateRandomChallengeSkeleton(User user, Category category);

    /**
     * Get the top five categories by relevance score for a user.
     *
     * @param user The user for whom the top five categories are retrieved.
     * @return The list of top five categories by relevance score.
     */
    List<UserCategoryStats> getTopFiveByRelevanceScore(User user);

    /**
     * Pick a category for a user.
     *
     * @param user The user for whom the category is picked.
     * @return The picked category.
     */
    Category pickCategory(User user);

    /**
     * Generate a random curated challenge for a user.
     *
     * @param user The user for whom the challenge is generated.
     * @return The generated challenge.
     */
    ChallengeSkeleton generateRandomCuratedChallenge(User user);

    /**
     * Generate a curated challenge based on a specific duration for a user.
     *
     * @param user     The user for whom the challenge is generated.
     * @param duration The duration of the challenge.
     * @return The generated challenge.
     */
    ChallengeSkeleton generateCuratedChallengeByDuration(User user, int duration);

    /**
     * Generate a curated challenge based on a specific sum for a user.
     *
     * @param user The user for whom the challenge is generated.
     * @param sum  The sum of the challenge.
     * @return The generated challenge.
     */
    ChallengeSkeleton generateCuratedChallengeBySum(User user, int sum);
}