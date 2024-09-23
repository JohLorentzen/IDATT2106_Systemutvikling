package org.savingapp.service;

import lombok.AllArgsConstructor;
import org.savingapp.enums.Category;
import org.savingapp.model.ChallengeSkeleton;
import org.savingapp.model.User;
import org.savingapp.model.UserCategoryStats;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Implementation of the GenerateChallengeService.
 */
@Service
@AllArgsConstructor
public class GenerateChallengeServiceImpl implements GenerateChallengeService {

    /**
     * Calculates the daily rate for a given category.
     *
     * @param category The category to calculate the daily rate for.
     * @return The daily rate.
     */
    @Override
    public double calculateDailyRate(User user, Category category) {
        float categoryOffset = user.getUserInsight().getCategoryStats().stream()
                .filter(userCategoryStats -> userCategoryStats.getCategory().equals(category))
                .findFirst()
                .map(UserCategoryStats::getEfficiencyScoreOffset)
                .orElse(0f);
        double categoryTrend = user.getUserInsight().getCategoryStats().stream()
                .filter(userCategoryStats -> userCategoryStats.getCategory().equals(category))
                .findFirst()
                .map(UserCategoryStats::getTrend)
                .orElse(0d);
        float baseEfficiencyScore = user.getUserInsight().calculateBaseEfficiencyScore();

        return (baseEfficiencyScore + categoryOffset) * categoryTrend / 30;
    }

    /**
     * Generates a challenge skeleton with a given duration.
     *
     * @param category The category of the challenge to generate.
     * @param duration The duration the challenge will have.
     * @return The generated challenge skeleton.
     */
    @Override
    public ChallengeSkeleton generateSkeletonFromDuration(User user, Category category, int duration) {
        double projectedDailyRate = calculateDailyRate(user, category);
        int sum = (int) (duration * projectedDailyRate);

        // TODO: Adjust based on some parameters?
        sum = sum - (sum % 5);

        return new ChallengeSkeleton(projectedDailyRate, category, duration, sum);
    }

    /**
     * Generates a challenge skeleton with a given sum.
     *
     * @param category The category of the challenge to generate.
     * @param sum      The sum the challenge will have.
     * @return The generated challenge skeleton.
     */
    @Override
    public ChallengeSkeleton generateChallengeSkeletonFromSum(User user, Category category, int sum) {
        double projectedDailyRate = calculateDailyRate(user, category);
        int floorDuration = (int) (sum / projectedDailyRate);
        int ceilDuration = (int) Math.ceil(sum / projectedDailyRate);

        int floorDifference = Math.abs(sum - (int) (floorDuration * projectedDailyRate));
        int ceilDifference = Math.abs(sum - (int) (ceilDuration * projectedDailyRate));

        int duration = (floorDifference <= ceilDifference) ? floorDuration : ceilDuration;

        // TODO: Adjust based on daily rate?
        if (duration > 7 && duration < 21) duration = 14;
        else if (duration >= 21) duration = 30;

        return new ChallengeSkeleton(projectedDailyRate, category, duration, sum);
    }

    /**
     * Generates a random challenge skeleton.
     *
     * @param category The category of the challenge to generate.
     * @return The generated challenge skeleton.
     */
    @Override
    public ChallengeSkeleton generateRandomChallengeSkeleton(User user, Category category) {
        Random random = new Random();
        int duration = random.nextInt(30) + 1;

        // TODO: Adjust based on efficiency or other parameters?
        if (duration > 7 && duration < 21) duration = 14;
        else if (duration >= 21) duration = 30;

        return generateSkeletonFromDuration(user, category, duration);
    }

    /**
     * Gets the top five categories by relevance score.
     *
     * @return The top five categories by relevance score.
     */
    @Override
    public List<UserCategoryStats> getTopFiveByRelevanceScore(User user) {
        return user.getUserInsight().getCategoryStats().stream()
                .sorted(Comparator.comparing(UserCategoryStats::getRelevanceScore).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    /**
     * Picks a random category from the top five categories by relevance score.
     *
     * @return The picked category.
     */
    @Override
    public Category pickCategory(User user) {

        List<UserCategoryStats> topFive = getTopFiveByRelevanceScore(user);

        if (topFive.isEmpty()) {
            List<Category> userCategories = user.getUserInsight().getCategories();
            List<Category> allCategories = new ArrayList<>(Arrays.stream(Category.values()).toList());
            List<UserCategoryStats> mockedStats;

            if (!userCategories.isEmpty()) {
                allCategories.removeAll(userCategories);
                mockedStats = List.of(
                        UserCategoryStats.builder().category(userCategories.get(0)).build(),
                        UserCategoryStats.builder().category(userCategories.get(1)).build(),
                        UserCategoryStats.builder().category(userCategories.get(2)).build(),
                        UserCategoryStats.builder().category(allCategories.get(0)).build(),
                        UserCategoryStats.builder().category(allCategories.get(1)).build()
                );
            } else {
                mockedStats = List.of(
                        UserCategoryStats.builder().category(allCategories.get(0)).build(),
                        UserCategoryStats.builder().category(allCategories.get(1)).build(),
                        UserCategoryStats.builder().category(allCategories.get(2)).build(),
                        UserCategoryStats.builder().category(allCategories.get(3)).build(),
                        UserCategoryStats.builder().category(allCategories.get(4)).build()
                );
            }
            topFive = mockedStats;
        }

        Random random = new Random();
        return topFive.get(random.nextInt(5)).getCategory();
    }

    /**
     * Generates a random curated challenge.
     *
     * @return The generated challenge.
     */
    @Override
    public ChallengeSkeleton generateRandomCuratedChallenge(User user) {
        return generateRandomChallengeSkeleton(user, pickCategory(user));
    }

    /**
     * Generates a curated  challenge from user stats by duration.
     *
     * @param duration The duration of the challenge.
     * @return The generated challenge.
     */
    @Override
    public ChallengeSkeleton generateCuratedChallengeByDuration(User user, int duration) {
        return generateSkeletonFromDuration(user, pickCategory(user), duration);
    }

    /**
     * Generates a curated challenge from user stats by sum.
     *
     * @param sum The sum of the challenge.
     * @return The generated challenge.
     */
    @Override
    public ChallengeSkeleton generateCuratedChallengeBySum(User user, int sum) {
        return generateChallengeSkeletonFromSum(user, pickCategory(user), sum);
    }


}
