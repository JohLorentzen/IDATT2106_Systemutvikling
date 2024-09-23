package org.savingapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.savingapp.enums.Category;
import org.savingapp.enums.LifeSituation;
import org.savingapp.enums.SkillLevel;
import org.savingapp.model.ChallengeSkeleton;
import org.savingapp.model.User;
import org.savingapp.model.UserCategoryStats;
import org.savingapp.model.UserInsight;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenerateChallengeServiceImplTest {
    User user;
    UserInsight userInsight;

    GenerateChallengeServiceImpl generateChallengeServiceImpl;


    @BeforeEach
    public void populateUserCategoryStats() {
        this.user = new User();
        user.setPassword("test");
        user.setUsername("test");
        this.userInsight = new UserInsight();
        userInsight.setSkillLevel(SkillLevel.AVERAGE);
        userInsight.setLifeSituation(LifeSituation.WORKING);
        userInsight.setCategoryStats(new ArrayList<>());
        for (Category category : Category.values()) {
            UserCategoryStats userCategoryStats = new UserCategoryStats();
            userCategoryStats.setUserInsight(userInsight);
            userCategoryStats.setCategory(category);
            userCategoryStats.setTrend(2500);
            userCategoryStats.setEfficiencyScoreOffset(0.05f);
            userInsight.getCategoryStats().add(userCategoryStats);
        }
        userInsight.setUser(user);
        user.setUserInsight(userInsight);

        generateChallengeServiceImpl = new GenerateChallengeServiceImpl();
    }

    @Test
    public void testCalculateBaseEfficiencyScore() {
        float score = userInsight.calculateBaseEfficiencyScore();
        assertEquals(0.17f, score);
    }

    @Test
    public void testGenerateRandomChallengeSkeleton() {
        Category category = Category.GROCERIES;
        ChallengeSkeleton challengeSkeleton = generateChallengeServiceImpl.generateRandomChallengeSkeleton(user, category);
        System.out.println(challengeSkeleton.getCategory());
        assertEquals(category, challengeSkeleton.getCategory());
        System.out.println(challengeSkeleton.getEfficiency());
        System.out.println(challengeSkeleton.getSum());
        System.out.println(challengeSkeleton.getDuration());


        System.out.println("Save " + challengeSkeleton.getSum() + "kr on " + challengeSkeleton.getCategory() +
                " in " + challengeSkeleton.getDuration() + " days!\n");
    }

    @Test
    public void testGenerateSkeletonFromDuration() {
        Category category = Category.PETS;
        int duration = 14;
        ChallengeSkeleton challengeSkeleton = generateChallengeServiceImpl.generateSkeletonFromDuration(user,
                category, duration);
        assertEquals(category, challengeSkeleton.getCategory());
        assertEquals(duration, challengeSkeleton.getDuration());
        System.out.println(challengeSkeleton.getEfficiency());
        System.out.println(challengeSkeleton.getSum());
        System.out.println(challengeSkeleton.getDuration());
        System.out.println("Save " + challengeSkeleton.getSum() + "kr on " + challengeSkeleton.getCategory() +
                " in " + challengeSkeleton.getDuration() + " days!\n");
    }

    @Test
    public void testGenerateSkeletonFromSum() {
        Category category = Category.ENTERTAINMENT;
        int sum = 250;
        ChallengeSkeleton challengeSkeleton = generateChallengeServiceImpl.generateChallengeSkeletonFromSum(user,
                category, sum);
        assertEquals(category, challengeSkeleton.getCategory());
        assertEquals(sum, challengeSkeleton.getSum());
        System.out.println(challengeSkeleton.getEfficiency());
        System.out.println(challengeSkeleton.getSum());
        System.out.println(challengeSkeleton.getDuration());
        System.out.println("Save " + challengeSkeleton.getSum() + "kr on " + challengeSkeleton.getCategory() +
                " in " + challengeSkeleton.getDuration() + " days!\n");
    }

}
