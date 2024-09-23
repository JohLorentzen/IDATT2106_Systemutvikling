package org.savingapp.service;

import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import org.savingapp.dto.BudgetDTO;
import org.savingapp.dto.BudgetPostDTO;
import org.savingapp.enums.Category;
import org.savingapp.model.Budget;
import org.savingapp.model.BudgetPost;
import org.savingapp.model.Transaction;
import org.savingapp.repository.BudgetRepository;
import org.savingapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a budget service that handles business logic for budgets.
 * Implements BudgetService to provide methods for CRUD operations on budgets.
 */
@Service
@AllArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private BudgetRepository budgetRepository;
    private UserRepository userRepository;

    /**
     * Returns a list of all budgets for a user.
     *
     * @param userId the id of the user
     * @return a list of BudgetDTO objects
     */
    @Override
    public List<BudgetDTO> getAllUserBudgets(Long userId) {
        return budgetRepository.findByUserId(userId).stream().map(budget -> {
            BudgetDTO budgetDTO = new BudgetDTO();
            budgetDTO.setId(budget.getId());
            budgetDTO.setMonth(budget.getBudgetMonth().getValue());
            budgetDTO.setYear(budget.getBudgetYear().getValue());
            budgetDTO.setBudgetPosts(getBudgetPosts(budget.getId()));
            budgetDTO.setTotalBudgetedSum((int) calculateTotalBudgetedSum(budget));
            budgetDTO.setTotalSpentSum((int) calculateTotalSpentSum(budget));
            return budgetDTO;
        }).toList();
    }

    /**
     * Adds a budget post to a budget.
     *
     * @param budgetId      the id of the budget
     * @param budgetPostDTO the budget post to add
     */
    public void addBudgetPost(Long budgetId, BudgetPostDTO budgetPostDTO) {
        Budget budget = budgetRepository.findById(budgetId).orElseThrow();
        if (budget.getBudgetPosts().stream().anyMatch(bp -> bp.getCategory().equals(budgetPostDTO.getCategory()))) {
            throw new EntityExistsException("Budget post already exists");
        }
        BudgetPost budgetPost = new BudgetPost();
        budgetPost.setCategory(budgetPostDTO.getCategory());
        budgetPost.setBudgetedSum(budgetPostDTO.getBudgetedSum());
        budgetPost.setBudget(budget);
        budget.getBudgetPosts().add(budgetPost);
        budgetRepository.save(budget);
    }

    /**
     * Calculates the sum of all transactions for a budget post.
     *
     * @param budgetPost the budget post
     * @return the sum of all transactions
     */
    private double calculateSpentSum(BudgetPost budgetPost) {
        Budget budget = budgetPost.getBudget();
        int budgetMonth = budget.getBudgetMonth().getValue();
        int budgetYear = budget.getBudgetYear().getValue();

        return budget.getUser().getAccounts().stream()
                .flatMap(account -> account.getOutgoingTransactions().stream())
                .filter(transaction -> transaction.getCategory().equals(budgetPost.getCategory()))
                .filter(transaction -> {
                    LocalDateTime transactionDateTime = transaction.getTimestamp();
                    return transactionDateTime.getMonthValue() == budgetMonth && transactionDateTime.getYear() == budgetYear;
                })
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * Returns a list of budget posts for a budget.
     *
     * @param budgetId the id of the budget
     * @return a list of BudgetPostDTO objects
     */
    public List<BudgetPostDTO> getBudgetPosts(Long budgetId) {
        Budget budget = budgetRepository.findById(budgetId).orElseThrow();
        return budget.getBudgetPosts().stream().map(budgetPost -> {
            BudgetPostDTO budgetPostDTO = new BudgetPostDTO();
            budgetPostDTO.setId(budgetPost.getId());
            budgetPostDTO.setCategory(budgetPost.getCategory());
            budgetPostDTO.setBudgetedSum((int) budgetPost.getBudgetedSum());
            budgetPostDTO.setActualSum((int) calculateSpentSum(budgetPost));
            return budgetPostDTO;
        }).collect(Collectors.toList());
    }

    /**
     * Returns a budget for a given id.
     *
     * @param budgetId the id of the budget
     * @return a BudgetDTO object
     */
    public BudgetDTO getBudget(Long budgetId) {
        Budget budget = budgetRepository.findById(budgetId).orElseThrow();
        BudgetDTO budgetDTO = new BudgetDTO();
        budgetDTO.setId(budget.getId());
        budgetDTO.setMonth(budget.getBudgetMonth().getValue());
        budgetDTO.setYear(budget.getBudgetYear().getValue());
        budgetDTO.setTotalBudgetedSum((int) calculateTotalBudgetedSum(budget));
        budgetDTO.setTotalSpentSum((int) calculateTotalSpentSum(budget));
        budgetDTO.setBudgetPosts(getBudgetPosts(budget.getId()));
        return budgetDTO;
    }

    /**
     * Calculates the total budgeted sum for a budget.
     *
     * @param budget the budget
     * @return the total budgeted sum
     */
    private double calculateTotalBudgetedSum(Budget budget) {
        return budget.getBudgetPosts().stream()
                .mapToDouble(BudgetPost::getBudgetedSum)
                .sum();
    }

    /**
     * Calculates the total spent sum for a budget.
     *
     * @param budget the budget
     * @return the total spent sum
     */
    private double calculateTotalSpentSum(Budget budget) {
        return budget.getBudgetPosts().stream()
                .mapToDouble(this::calculateSpentSum)
                .sum();
    }


    /**
     * Returns a budget for a given month and year.
     *
     * @param userId the id of the user
     * @param month  the month of the budget
     * @param year   the year of the budget
     * @return a BudgetDTO object
     */
    public BudgetDTO getBudgetByMonthAndYear(Long userId, int month, int year) {
        Budget budget = budgetRepository.findByUserIdAndBudgetMonthAndBudgetYear(userId, Month.of(month), Year.of(year))
                .orElseGet(() -> {
                    Budget newBudget = new Budget();
                    newBudget.setBudgetMonth(Month.of(month));
                    newBudget.setBudgetYear(Year.of(year));
                    newBudget.setUser(userRepository.findById(userId).orElseThrow());
                    newBudget.setBudgetPosts(Collections.emptyList());
                    return budgetRepository.save(newBudget);
                });

        BudgetDTO budgetDTO = new BudgetDTO();
        budgetDTO.setId(budget.getId());
        budgetDTO.setMonth(budget.getBudgetMonth().getValue());
        budgetDTO.setYear(budget.getBudgetYear().getValue());
        budgetDTO.setTotalBudgetedSum((int) calculateTotalBudgetedSum(budget));
        budgetDTO.setTotalSpentSum((int) calculateTotalSpentSum(budget));
        budgetDTO.setBudgetPosts(getBudgetPosts(budget.getId()));
        return budgetDTO;
    }

    /**
     * Creates a budget for the current month and year.
     *
     * @param userId the id of the user
     * @return a BudgetDTO object
     */
    public BudgetDTO createBudget(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        if (budgetRepository.findByUserIdAndBudgetMonthAndBudgetYear(userId, now.getMonth(), Year.of(now.getYear())).isPresent()) {
            return getBudgetByMonthAndYear(userId, now.getMonthValue(), now.getYear());
        }
        Budget budget = new Budget();
        budget.setBudgetMonth(now.getMonth());
        budget.setBudgetYear(Year.of(now.getYear()));
        budget.setUser(userRepository.findById(userId).orElseThrow());
        budget.setBudgetPosts(Collections.emptyList());
        budgetRepository.save(budget);
        return getBudget(budget.getId());
    }

    /**
     * Adds a budget to a user.
     *
     * @param userId    the id of the user
     * @param budgetDTO the budget to add
     */
    public void addBudget(Long userId, BudgetDTO budgetDTO) {
        Budget budget = new Budget();
        budget.setBudgetMonth(Month.of(budgetDTO.getMonth()));
        budget.setBudgetYear(Year.of(budgetDTO.getYear()));
        budget.setUser(userRepository.findById(userId).orElseThrow());
        budgetRepository.save(budget);
    }

    /**
     * Returns the active budget for a user.
     *
     * @param userId the id of the user
     * @return a BudgetDTO object
     */
    public BudgetDTO getActiveBudget(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        int month = now.getMonthValue();
        int year = now.getYear();
        return getBudgetByMonthAndYear(userId, month, year);
    }


    /**
     * Deletes a budget for a given id.
     *
     * @param budgetId the id of the budget
     */
    public void deleteBudget(Long budgetId) {
        budgetRepository.deleteById(budgetId);
    }

    /**
     * Updates a budget post for a given id.
     *
     * @param budgetId      the id of the budget
     * @param budgetPostDTO the budget post to update
     */
    public void updateBudgetPost(Long budgetId, BudgetPostDTO budgetPostDTO) {
        Budget budget = budgetRepository.findById(budgetId).orElseThrow();
        BudgetPost budgetPost = budget.getBudgetPosts().stream()
                .filter(bp -> bp.getCategory().equals(budgetPostDTO.getCategory()))
                .findFirst()
                .orElseThrow();
        budgetPost.setBudgetedSum(budgetPostDTO.getBudgetedSum());
        budgetRepository.save(budget);
    }

    /**
     * Deletes a budget post for a given id.
     *
     * @param budgetId the id of the budget
     * @param category the category of the budget post
     */
    public void deleteBudgetPost(Long budgetId, Category category) {
        Budget budget = budgetRepository.findById(budgetId).orElseThrow();
        BudgetPost budgetPost = budget.getBudgetPosts().stream()
                .filter(bp -> bp.getCategory().equals(category))
                .findFirst()
                .orElseThrow();
        budget.getBudgetPosts().remove(budgetPost);
        budgetRepository.save(budget);
    }


}
