package org.savingapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.savingapp.enums.*;
import org.savingapp.model.*;
import org.savingapp.repository.BudgetRepository;
import org.savingapp.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;


public class BudgetServiceImplTest {

    User user;

    @Mock
    BudgetServiceImpl budgetService;

    @Mock
    UserRepository userRepository;
    @Mock
    BudgetRepository budgetRepository;


    @BeforeEach
    void createUserData() {
        this.user = new User();
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
            transaction.setCategory(Category.values()[j % 15]);
            transaction.setTimestamp(LocalDateTime.now().minusDays(j + 30));
            transaction.setFromAccount(account);
            transaction.setToAccount(account);
            transactionList.add(transaction);
        }
        account.setOutgoingTransactions(transactionList);
        user.setAccounts(List.of(account));

        Budget budget = new Budget();
        budget.setBudgetMonth(Month.APRIL);
        budget.setBudgetYear(Year.of(2024));

        List<BudgetPost> budgetPosts = new ArrayList<>();
        for (int j = 0; j < 15; j++) {
            BudgetPost budgetPost = new BudgetPost();
            budgetPost.setCategory(Category.values()[j]);
            budgetPost.setBudgetedSum(100.0 * (j));
            budgetPost.setBudget(budget);
            budgetPosts.add(budgetPost);
            budget.setBudgetPosts(budgetPosts);
        }
        budget.setUser(user);
        budgetService = new BudgetServiceImpl(budgetRepository, userRepository);
    }


}


