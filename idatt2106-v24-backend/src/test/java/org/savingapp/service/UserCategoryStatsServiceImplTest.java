package org.savingapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.savingapp.enums.*;
import org.savingapp.model.*;
import org.savingapp.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserCategoryStatsServiceImplTest {


    @InjectMocks
    private UserCategoryStatsServiceImpl userCategoryStatsServiceImpl;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        UserInsight userInsight = new UserInsight();
        user.setUsername("test");
        user.setPassword("test");
        user.setFullName("test testersson");
        userInsight.setSkillLevel(SkillLevel.AVERAGE);
        userInsight.setLifeSituation(LifeSituation.WORKING);
        userInsight.setInterests(List.of(InterestEnum.STOCKS));
        userInsight.setCategories(List.of(Category.HOME));
        user.setUserInsight(userInsight);
        userInsight.setUser(user);

        Account account = new Account();
        account.setUser(user);
        account.setBalance(1000.0);
        account.setName("My Account");
        account.setType(AccountType.CHECKING);

        List<Transaction> transactionList = new ArrayList<>();
        for (int j = 1; j <= 60; j++) {
            Transaction transaction = new Transaction();
            transaction.setAmount(500.0 * j);
            transaction.setCategory(Category.values()[j%15]);
            transaction.setTimestamp(LocalDateTime.now().minusDays(j+30));
            transaction.setFromAccount(account);
            transaction.setToAccount(account);
            transactionList.add(transaction);
        }
        account.setOutgoingTransactions(transactionList);
        user.setAccounts(List.of(account));

        userCategoryStatsServiceImpl = new UserCategoryStatsServiceImpl(userRepository);

    }

    @Test
    public void testInitUserCategoryStats() {
        userCategoryStatsServiceImpl.initUserCategoryStats(user);
        System.out.println(user.getUserInsight().getCategoryStats());
    }

    @Test
    public void testGetCategoryStats() {
        userCategoryStatsServiceImpl.initUserCategoryStats(user);
        UserCategoryStats testStats = userCategoryStatsServiceImpl.getCategoryStats(user, Category.HOME);
        assertEquals(Category.HOME, testStats.getCategory());
        assertEquals(0.1f, testStats.getRelevanceScore());
        assertEquals(0f, testStats.getEfficiencyScoreOffset());
    }

    @Test
    public void testUpdateCategoryStats() {
        userCategoryStatsServiceImpl.initUserCategoryStats(user);
        userCategoryStatsServiceImpl.updateCategoryStats(user, Category.HOME, 100,
                0.7f, 0.1f);
        UserCategoryStats testStats = userCategoryStatsServiceImpl.getCategoryStats(user, Category.HOME);
        assertEquals(Category.HOME, testStats.getCategory());
        assertEquals(100, testStats.getTrend());
        assertEquals(0.7f, testStats.getRelevanceScore());
        assertEquals(0.1f, testStats.getEfficiencyScoreOffset());
    }

    @Test
    public void testUpdateRelevance() {
        userCategoryStatsServiceImpl.initUserCategoryStats(user);
        userCategoryStatsServiceImpl.updateRelevance(user, Category.HOME, 0.7f);
        UserCategoryStats testStats = userCategoryStatsServiceImpl.getCategoryStats(user, Category.HOME);
        assertEquals(Category.HOME, testStats.getCategory());
        assertEquals(0.7f, testStats.getRelevanceScore());
        assertEquals(0f, testStats.getEfficiencyScoreOffset());
    }

    @Test
    public void testUpdateEfficiency() {
        userCategoryStatsServiceImpl.initUserCategoryStats(user);
        userCategoryStatsServiceImpl.updateEfficiency(user, Category.HOME, 0.1f);
        UserCategoryStats testStats = userCategoryStatsServiceImpl.getCategoryStats(user, Category.HOME);
        assertEquals(Category.HOME, testStats.getCategory());

        assertEquals(0.1f, testStats.getRelevanceScore());
        assertEquals(0.1f, testStats.getEfficiencyScoreOffset());
    }

    @Test
    public void testUpdateTrend() {
        userCategoryStatsServiceImpl.initUserCategoryStats(user);
        userCategoryStatsServiceImpl.updateTrend(user, Category.HOME, 100);
        UserCategoryStats testStats = userCategoryStatsServiceImpl.getCategoryStats(user, Category.HOME);
        assertEquals(Category.HOME, testStats.getCategory());
        assertEquals(100, testStats.getTrend());
        assertEquals(0.1f, testStats.getRelevanceScore());
        assertEquals(0f, testStats.getEfficiencyScoreOffset());
    }


}