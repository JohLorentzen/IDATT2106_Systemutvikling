package org.savingapp.config;

import lombok.AllArgsConstructor;
import org.savingapp.enums.*;
import org.savingapp.model.*;
import org.savingapp.repository.*;
import org.savingapp.service.UserCategoryStatsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Configuration class for the database.
 */
@Configuration
@AllArgsConstructor
public class DatabaseLoader {

    private final PasswordEncoder passwordEncoder;
    private final UserInsightRepository userInsightRepository;
    private final ChallengeRepository challengeRepository;
    private final SavingsGoalRepository goalRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;
    private final UserCategoryStatsService userCategoryStatsService;
    private final SavingsPathRepository savingPathRepository;
    private final CompletedUserChallengeRepository completedUserChallengeRepository;


    /**
     * Initialize the database with dummy data.
     *
     * @return The dummy data.
     */
    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            for (int i = 1; i <= 5; i++) {
                User user = new User();
                user.setFullName("Testar Brukersen " + i);
                user.setUsername("testuser" + i);
                user.setId((long) i);
                user.setPassword(passwordEncoder.encode("password"));
                userRepository.save(user);

                Account account = new Account();
                account.setUser(user);
                account.setBalance(1000.0 * i);
                account.setName("My Account " + i);
                account.setType(i % 2 == 0 ? AccountType.CHECKING : AccountType.SAVINGS);
                accountRepository.save(account);

                UserInsight userInsight = new UserInsight();
                userInsight.setLifeSituation(i % 2 == 0 ? LifeSituation.STUDENT : LifeSituation.WORKING);
                userInsight.setCategories(List.of(Category.FOOD_AND_DRINKS, Category.SHOPPING, Category.TRANSPORTATION));
                userInsight.setSkillLevel(i % 2 == 0 ? SkillLevel.INTERMEDIATE : SkillLevel.AVERAGE);
                userInsight.setUser(user);
                userInsight.setInterests(List.of(InterestEnum.CRYPTOCURRENCY, InterestEnum.STOCKS, InterestEnum.RETIREMENT));
                userInsightRepository.save(userInsight);

                SavingsGoal goal = new SavingsGoal();
                goal.setTitle("Oslotur");
                goal.setSavedAmount(400.0);
                goal.setGoalAmount(800.0);
                goal.setEndDate(LocalDate.now().plusDays(60));
                goal.setActive(Boolean.TRUE);
                goal.setUser(user);
                goalRepository.save(goal);
                user.setSavingGoals(List.of(goal));

                SavingsPath savingsPath = new SavingsPath(null, goal, new ArrayList<>());
                savingPathRepository.save(savingsPath);

                for (int j = 1; j <= 4; j++) {
                    Random random = new Random();
                    Challenge challenge = new Challenge();
                    challenge.setCategory(Category.values()[random.nextInt(Category.values().length)]);
                    challenge.setName(challenge.getCategory().getChallengeTitle());
                    challenge.setDuration(j % 2 == 0 ? 30 : 7);
                    challenge.setDescription("Spar " + 100.00 * j + "kr på " + challenge.getCategory().getNorwegianText() +
                            " på " + challenge.getDuration() + " dager!");
                    challenge.setLifeSituation(j % 2 == 0 ? LifeSituation.WORKING : LifeSituation.STUDENT);
                    challengeRepository.save(challenge);


                    UserChallenge userChallenge = new UserChallenge();
                    userChallenge.setUser(user);
                    userChallenge.setChallenge(challenge);
                    userChallenge.setFromDate(j % 2 == 0 ?
                            LocalDate.now() :
                            LocalDate.now().minusDays(challenge.getDuration() * 3L + 3L));
                    userChallenge.setToDate(j % 2 == 0 ?
                            LocalDate.now().plusDays(challenge.getDuration()) :
                            LocalDate.now().minusDays(challenge.getDuration() * 3L));
                    userChallenge.setAmountSaved(j % 2 == 0 ? 100.00 : 50.00);
                    userChallenge.setGoalAmount(100.00);
                    completedUserChallengeRepository.save(new CompletedUserChallenge(
                            null, userChallenge, 100.00 * j, savingsPath));
                }


                int[] categories = new int[]{0, 1, 3, 4, 5, 6, 9, 10};  // Focused categories
                if (i > 1) {
                    Random random = new Random();

                    int numberOfTransactionsPerMonth = 60;  // Standardize the number of transactions

                    for (int month = 4; month >= 2; month--) {
                        for (int j = 1; j <= numberOfTransactionsPerMonth; j++) {
                            // Create a new transaction
                            Transaction transaction = new Transaction();

                            int categoryIndex = categories[j % categories.length];
                            Category category = Category.values()[categoryIndex];

                            // Consistent amount calculation
                            double baseAmount = categoryIndex == 0 || categoryIndex == 1
                                    || categoryIndex == 3 ? 160.0 : 40.0;  // Higher for focused categories
                            double amountVariation = 20.0 * random.nextDouble();  // Slight variation
                            transaction.setAmount(baseAmount + amountVariation);

                            // Set the other properties
                            transaction.setCategory(category);
                            transaction.setTimestamp(LocalDateTime.now().minusDays((month == 2 ? 30 : 0) + j % 10));
                            transaction.setFromAccount(accountRepository.findById((long) (i - 1)).get());
                            transaction.setToAccount(account);
                            transactionRepository.save(transaction);
                        }
                    }
                }
                for (int month = 5; month >= 3; month--) {
                    Budget budget = new Budget();
                    budget.setBudgetMonth(Month.of(month));
                    budget.setBudgetYear(Year.of(LocalDateTime.now().getYear()));
                    budget.setUser(user);

                    List<BudgetPost> budgetPosts = new ArrayList<>();
                    for (int j = 0; j < categories.length; j++) {
                        Random random = new Random();
                        BudgetPost budgetPost = new BudgetPost();
                        budgetPost.setCategory(Category.values()[categories[j]]);
                        int categoryIndex = categories[j % categories.length];
                        double baseAmount = categoryIndex == 0 || categoryIndex == 1
                                || categoryIndex == 3 ? 2300.0 : 800.0;  // Higher for focused categories
                        double amountVariation = 40.0 * random.nextDouble();  // Slight variation

                        budgetPost.setBudgetedSum((int) (baseAmount + amountVariation));
                        budgetPost.setBudget(budget);
                        budgetPosts.add(budgetPost);
                        budget.setBudgetPosts(budgetPosts);
                    }
                    budgetRepository.save(budget);
                }
                userRepository.save(user);

            }
            for (int i = 1; i <= 5; i++) {
                User user = userRepository.findById((long) i).get();
                userCategoryStatsService.initUserCategoryStats(user);
            }
        };
    }
}