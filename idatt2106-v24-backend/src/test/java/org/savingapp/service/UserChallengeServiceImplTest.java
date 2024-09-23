package org.savingapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.savingapp.dto.ChallengeDTO;
import org.savingapp.dto.UserChallengeDTO;
import org.savingapp.enums.*;
import org.savingapp.model.*;
import org.savingapp.repository.*;
import org.savingapp.util.UserUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserChallengeServiceImplTest {

    @InjectMocks
    ChallengeServiceImpl challengeService;
    @Mock
    private UserChallengeRepository userChallengeRepository;
    @Mock
    private ChallengeRepository challengeRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserChallengeServiceImpl userChallengeService;
    private User user;

    @Mock
    private UserUtil userUtil;

    @Mock
    private GenerateChallengeServiceImpl generateChallengeServiceImpl;

    @Mock
    private SavingsPathServiceImpl savingsPathService;

    @Mock
    private SavingsPathRepository savingsPathRepository;

    @Mock
    private SavingsGoalRepository savingsGoalRepository;

    @Mock
    private CompletedUserChallengeRepository completedUserChallengeRepository;

    @InjectMocks
    private SavingsGoalServiceImpl savingsGoalService;

    @Mock
    private UserCategoryStatsServiceImpl ucss;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    void createUserData() {
        this.user = new User();
        UserInsight userInsight = new UserInsight();
        user.setUsername("test");
        user.setPassword("test");
        user.setFullName("test testersson");
        user.setUserChallenges(new ArrayList<>());
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


        this.generateChallengeServiceImpl = new GenerateChallengeServiceImpl();
        this.challengeService = new ChallengeServiceImpl(userUtil, generateChallengeServiceImpl, challengeRepository);
        this.ucss = new UserCategoryStatsServiceImpl(userRepository);

        ucss.initUserCategoryStats(user);

        this.userChallengeService = new UserChallengeServiceImpl(userChallengeRepository, challengeRepository,
                userRepository, challengeService, ucss, savingsPathService, completedUserChallengeRepository);

        ucss.updateCategoryStats(user, Category.TRANSPORTATION, 700, 0.6f, 0.05f);
        ucss.updateCategoryStats(user, Category.HOME, 1500, 0.6f, 0.04f);
        ucss.updateCategoryStats(user, Category.FOOD_AND_DRINKS, 3000, 0.8f, -0.01f);
        ucss.updateCategoryStats(user, Category.ENTERTAINMENT, 500, 0.6f, 0.03f);
        ucss.updateCategoryStats(user, Category.SHOPPING, 1700, 0.8f, -0.05f);
    }

    @Test
    void getUserChallengesReturnsExpectedResult() {
        UserChallenge userChallenge = new UserChallenge(1L, new User(), 100.0, 50.0,
                LocalDate.now(), LocalDate.now(), new Challenge());
        List<UserChallenge> userChallengeList = Collections.singletonList(userChallenge);
        Page<UserChallenge> userChallengePage = new PageImpl<>(userChallengeList);

        when(userChallengeRepository.findByUser(any(Pageable.class), any(User.class))).thenReturn(userChallengePage);

        Pageable pageable = PageRequest.of(0, 4);
        User user = new User();
        Page<UserChallengeDTO> result = userChallengeService.getUserChallenges(pageable, user);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void saveUserChallengeSavesExpectedUserChallenge() {
        ChallengeDTO challengeDTO = ChallengeDTO.builder()
                .id(1L)
                .name("Challenge")
                .description("Try to drive less!")
                .category(Category.TRANSPORTATION)
                .lifeSituation(LifeSituation.STUDENT)
                .duration(2)
                .sum(400)
                .averageAmount(100.0)
                .build();

        UserChallengeDTO userChallengeDTO = new UserChallengeDTO(1L, 100.0, 50.0,
                LocalDate.now(), LocalDate.now(),
                challengeDTO);

        User user = new User();
        Challenge challenge = new Challenge();

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(challengeRepository.findById(anyLong())).thenReturn(Optional.of(challenge));

        userChallengeService.saveUserChallenge(userChallengeDTO, user);

        verify(userChallengeRepository, times(1)).save(any(UserChallenge.class));
    }

    @Test
    void saveUserChallengeThrowsExceptionWhenUserNotFound() {
        ChallengeDTO challengeDTO = ChallengeDTO.builder()
                .id(1L)
                .name("Challenge")
                .description("Try to drive less!")
                .category(Category.TRANSPORTATION)
                .lifeSituation(LifeSituation.STUDENT)
                .duration(2)
                .sum(400)
                .averageAmount(100.0)
                .build();

        UserChallengeDTO userChallengeDTO = new UserChallengeDTO(1L, 100.0, 50.0,
                LocalDate.now(), LocalDate.now(),
                challengeDTO);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userChallengeService.saveUserChallenge(userChallengeDTO,
                any(User.class)));
    }

    @Test
    void saveUserChallengeThrowsExceptionWhenChallengeNotFound() {
        ChallengeDTO challengeDTO = ChallengeDTO.builder()
                .id(1L)
                .name("Challenge")
                .description("Try to drive less!")
                .category(Category.TRANSPORTATION)
                .lifeSituation(LifeSituation.STUDENT)
                .duration(2)
                .sum(400)
                .averageAmount(100.0)
                .build();

        UserChallengeDTO userChallengeDTO = new UserChallengeDTO(1L, 100.0, 50.0,
                LocalDate.now(), LocalDate.now(),
                challengeDTO);

        User user = new User();

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(challengeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userChallengeService.saveUserChallenge(userChallengeDTO,
                any(User.class)));
    }

    @Test
    void testCreateRandomUserChallenge() {
        createUserData();
        ucss.initUserCategoryStats(user);


        UserChallenge testChallenge = userChallengeService.createRandomUserChallenge(user);
        System.out.println("----RANDOMLY GENERATED WITHOUT DATA---\n");
        System.out.println("----USERCHALLENGE---");
        System.out.println("Goal Amount: " + testChallenge.getGoalAmount());
        System.out.println("Start Date: " + testChallenge.getFromDate());
        System.out.println("End date: " + testChallenge.getToDate());
        System.out.println("\n----CHALLENGE---");
        System.out.println("Trend: " + ucss.getCategoryStats(user,
                testChallenge.getChallenge().getCategory()).getTrend());
        System.out.println("Relevance: " + ucss.getCategoryStats(user,
                testChallenge.getChallenge().getCategory()).getRelevanceScore());
        System.out.println("Skill: " + ucss.getCategoryStats(user,
                testChallenge.getChallenge().getCategory()).getEfficiencyScoreOffset());
        System.out.println("Category: " + testChallenge.getChallenge().getCategory());
        System.out.println("Situation: " + testChallenge.getChallenge().getLifeSituation());
        System.out.println("Challenge sum: " + testChallenge.getChallenge().getSum());
        System.out.println("Average amount: " + String.format("%.2f",
                testChallenge.getChallenge().getAverageAmount()) + "kr per day");
        System.out.println("Duration: " + testChallenge.getChallenge().getDuration() + " days");
        System.out.println("Description: " + testChallenge.getChallenge().getDescription());
        System.out.println("Name:" + testChallenge.getChallenge().getName());
        System.out.println();

    }

    @Test
    void testCurateChallenge() {
        createUserData();

        UserChallenge testChallenge = userChallengeService.createRandomUserChallenge(user);

        System.out.println("----RANDOMLY GENERATED---\n");
        System.out.println("----USERCHALLENGE---");
        System.out.println("Goal Amount: " + testChallenge.getGoalAmount());
        System.out.println("Start Date: " + testChallenge.getFromDate());
        System.out.println("End date: " + testChallenge.getToDate());
        System.out.println("\n----CHALLENGE---");
        System.out.println("Trend: " + ucss.getCategoryStats(user,
                testChallenge.getChallenge().getCategory()).getTrend());
        System.out.println("Relevance: " + ucss.getCategoryStats(user,
                testChallenge.getChallenge().getCategory()).getRelevanceScore());
        System.out.println("Skill: " + ucss.getCategoryStats(user,
                testChallenge.getChallenge().getCategory()).getEfficiencyScoreOffset());
        System.out.println("Category: " + testChallenge.getChallenge().getCategory());
        System.out.println("Situation: " + testChallenge.getChallenge().getLifeSituation());
        System.out.println("Challenge sum: " + testChallenge.getChallenge().getSum());
        System.out.println("Average amount: " + String.format("%.2f",
                testChallenge.getChallenge().getAverageAmount()) + "kr per day");
        System.out.println("Duration: " + testChallenge.getChallenge().getDuration() + " days");
        System.out.println("Description: " + testChallenge.getChallenge().getDescription());
        System.out.println("Name:" + testChallenge.getChallenge().getName());
        System.out.println();

        assertNotEquals(0, testChallenge.getGoalAmount());
        assertNotEquals(null, testChallenge.getFromDate());
        assertNotEquals(null, testChallenge.getToDate());
        assertNotEquals(null, testChallenge.getChallenge().getCategory());
        assertNotEquals(null, testChallenge.getChallenge().getLifeSituation());
        assertNotEquals(0, testChallenge.getChallenge().getSum());
        assertNotEquals(0, testChallenge.getChallenge().getDuration());
        assertNotEquals(0, testChallenge.getChallenge().getSum());
    }

    @Test
    void testCurateByDuration() {
        createUserData();

        UserChallenge testChallenge = userChallengeService.createUserChallengeByDuration(user, 7);

        System.out.println("----CURATED BY DURATION---\n");
        System.out.println("----USERCHALLENGE---");
        System.out.println("Goal Amount: " + testChallenge.getGoalAmount());
        System.out.println("Start Date: " + testChallenge.getFromDate());
        System.out.println("End date: " + testChallenge.getToDate());
        System.out.println("\n----CHALLENGE---");
        System.out.println("Trend: " + ucss.getCategoryStats(user,
                testChallenge.getChallenge().getCategory()).getTrend());
        System.out.println("Relevance: " + ucss.getCategoryStats(user,
                testChallenge.getChallenge().getCategory()).getRelevanceScore());
        System.out.println("Skill: " + ucss.getCategoryStats(user,
                testChallenge.getChallenge().getCategory()).getEfficiencyScoreOffset());
        System.out.println("Category: " + testChallenge.getChallenge().getCategory());
        System.out.println("Situation: " + testChallenge.getChallenge().getLifeSituation());
        System.out.println("Challenge sum: " + testChallenge.getChallenge().getSum());
        System.out.println("Average amount: " + String.format("%.2f",
                testChallenge.getChallenge().getAverageAmount()) + "kr per day");
        System.out.println("Duration: " + testChallenge.getChallenge().getDuration() + " days");
        System.out.println("Description: " + testChallenge.getChallenge().getDescription());
        System.out.println("Name:" + testChallenge.getChallenge().getName());
        System.out.println();

        assertNotEquals(0, testChallenge.getGoalAmount());
        assertNotEquals(null, testChallenge.getFromDate());
        assertNotEquals(null, testChallenge.getToDate());
        assertNotEquals(null, testChallenge.getChallenge().getCategory());
        assertNotEquals(null, testChallenge.getChallenge().getLifeSituation());
        assertNotEquals(0, testChallenge.getChallenge().getSum());
        assertNotEquals(0, testChallenge.getChallenge().getDuration());
        assertNotEquals(0, testChallenge.getChallenge().getSum());

    }

    @Test
    void testCurateBySum() {
        createUserData();


        UserChallenge testChallenge = userChallengeService.createUserChallengeBySum(user, 250);


        System.out.println("----CURATED BY SUM----\n");
        System.out.println("----USERCHALLENGE---");
        System.out.println("Goal Amount: " + testChallenge.getGoalAmount());
        System.out.println("Start Date: " + testChallenge.getFromDate());
        System.out.println("End date: " + testChallenge.getToDate());
        System.out.println("\n----CHALLENGE---");
        System.out.println("Trend: " + ucss.getCategoryStats(user,
                testChallenge.getChallenge().getCategory()).getTrend());
        System.out.println("Relevance: " + ucss.getCategoryStats(user,
                testChallenge.getChallenge().getCategory()).getRelevanceScore());
        System.out.println("Skill: " + ucss.getCategoryStats(user,
                testChallenge.getChallenge().getCategory()).getEfficiencyScoreOffset());
        System.out.println("Category: " + testChallenge.getChallenge().getCategory());
        System.out.println("Situation: " + testChallenge.getChallenge().getLifeSituation());
        System.out.println("Challenge sum: " + testChallenge.getChallenge().getSum());
        System.out.println("Average amount: " + String.format("%.2f",
                testChallenge.getChallenge().getAverageAmount()) + "kr per day");
        System.out.println("Duration: " + testChallenge.getChallenge().getDuration() + " days");
        System.out.println("Description: " + testChallenge.getChallenge().getDescription());
        System.out.println("Name:" + testChallenge.getChallenge().getName());
        System.out.println();

        assertNotEquals(0, testChallenge.getGoalAmount());
        assertNotEquals(null, testChallenge.getFromDate());
        assertNotEquals(null, testChallenge.getToDate());
        assertNotEquals(null, testChallenge.getChallenge().getCategory());
        assertNotEquals(null, testChallenge.getChallenge().getLifeSituation());
        assertNotEquals(0, testChallenge.getChallenge().getSum());
        assertNotEquals(0, testChallenge.getChallenge().getDuration());
        assertNotEquals(0, testChallenge.getChallenge().getSum());

    }

    @Test
    void testAcceptChallenge() {
        createUserData();
        UserChallenge testChallenge = userChallengeService.createUserChallengeByDuration(user, 7);

        float oldRelevance = ucss.getCategoryStats(user,
                testChallenge.getChallenge().getCategory()).getRelevanceScore();
        userChallengeService.acceptChallenge(testChallenge);
        float newRelevance = ucss.getCategoryStats(user,
                testChallenge.getChallenge().getCategory()).getRelevanceScore();
        assertEquals(oldRelevance - 1.9f, newRelevance);
        assertTrue(user.getUserChallenges().contains(testChallenge));
    }

    @Test
    void testRejectChallenge() {
        createUserData();
        UserChallenge testChallenge = userChallengeService.createUserChallengeByDuration(user, 7);

        float oldRelevance = ucss.getCategoryStats(user,
                testChallenge.getChallenge().getCategory()).getRelevanceScore();
        userChallengeService.rejectChallenge(user, testChallenge.getChallenge());
        float newRelevance = ucss.getCategoryStats(user,
                testChallenge.getChallenge().getCategory()).getRelevanceScore();
        assertEquals(oldRelevance - 0.1f, newRelevance);
        assertFalse(user.getUserChallenges().contains(testChallenge));
    }

    @Test
    void testFinishChallengeWin() {
        createUserData();
        UserChallenge testChallenge = userChallengeService.createUserChallengeByDuration(user, 7);
        userChallengeService.acceptChallenge(testChallenge);
        assertTrue(user.getUserChallenges().contains(testChallenge));
        testChallenge.setAmountSaved(100000000.0);

        this.savingsPathService = new SavingsPathServiceImpl(savingsPathRepository, savingsGoalRepository,
                userChallengeRepository, completedUserChallengeRepository, savingsGoalService);


        float oldEfficiency = ucss.getCategoryStats(user,
                testChallenge.getChallenge().getCategory()).getEfficiencyScoreOffset();
        userChallengeService.finishChallenge(testChallenge);
        float newEfficiency = ucss.getCategoryStats(user,
                testChallenge.getChallenge().getCategory()).getEfficiencyScoreOffset();
        assertEquals(oldEfficiency + 0.01f, newEfficiency);
    }

    @Test
    void testFinishChallengeLose() {
        createUserData();
        UserChallenge testChallenge = userChallengeService.createUserChallengeByDuration(user, 7);
        userChallengeService.acceptChallenge(testChallenge);
        assertTrue(user.getUserChallenges().contains(testChallenge));
        testChallenge.setAmountSaved(0.0);

        // Set the end date of the test challenge to the past
        testChallenge.setToDate(LocalDate.now().minusDays(1));

        float oldEfficiency = ucss.getCategoryStats(user,
                testChallenge.getChallenge().getCategory()).getEfficiencyScoreOffset();
        float oldRelevance = ucss.getCategoryStats(user,
                testChallenge.getChallenge().getCategory()).getRelevanceScore();
        userChallengeService.finishChallenge(testChallenge);

        float newEfficiency = ucss.getCategoryStats(user,
                testChallenge.getChallenge().getCategory()).getEfficiencyScoreOffset();
        float newRelevance = ucss.getCategoryStats(user,
                testChallenge.getChallenge().getCategory()).getRelevanceScore();
        assertEquals(oldEfficiency - 0.01f, newEfficiency);
        assertEquals(oldRelevance + 1.9f, newRelevance);
    }

    @Test
    void testCalculateAmountSaved() {
        // Create test data
        createUserData();
        UserChallenge testChallenge = userChallengeService.createUserChallengeByDuration(user, 7);
        user.getUserChallenges().add(testChallenge);

        // Mock user's account and transactions
        Account mockedAccount = mock(Account.class);
        when(user.getAccounts()).thenReturn(List.of(mockedAccount));

        // Mock transactions for the month after the challenge start date
        LocalDate nextMonth = LocalDate.now().plusMonths(1);
        List<Transaction> mockedTransactions = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Transaction transaction = mock(Transaction.class);
            when(transaction.getAmount()).thenReturn(50.0);
            when(transaction.getCategory()).thenReturn(Category.FOOD_AND_DRINKS);
            when(transaction.getTimestamp()).thenReturn(nextMonth.atStartOfDay());
            mockedTransactions.add(transaction);
        }
        when(mockedAccount.getOutgoingTransactions()).thenReturn(mockedTransactions);

        // Call the method under test
        userChallengeService.calculateAmountSaved(user);

        // Calculate expected amount saved
        double averageTrend = 0.5; // Example average trend value (replace with the actual value)
        long daysSinceChallengeStart = LocalDate.now().toEpochDay() - testChallenge.getFromDate().toEpochDay();
        double expectedAmountSaved = averageTrend * daysSinceChallengeStart;

        // Assert that the amount saved for the challenge is as expected
        assertEquals(expectedAmountSaved, testChallenge.getAmountSaved(), 0.001); // Adjust delta according to precision requirements
    }


}