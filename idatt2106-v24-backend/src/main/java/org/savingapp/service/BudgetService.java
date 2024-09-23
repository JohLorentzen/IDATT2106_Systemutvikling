package org.savingapp.service;

import org.savingapp.dto.BudgetDTO;
import org.savingapp.dto.BudgetPostDTO;
import org.savingapp.enums.Category;

import java.util.List;

/**
 * This interface defines the contract for the BudgetService.
 * The BudgetService is responsible for managing budgets for the user.
 * This includes retrieving, saving, and deleting budgets and budget posts.
 */
public interface BudgetService {

    /**
     * Retrieve all budgets for a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of BudgetDTOs for the user.
     */
    List<BudgetDTO> getAllUserBudgets(Long userId);

    /**
     * Retrieve the active budget for a specific user.
     *
     * @param userId The ID of the user.
     * @return The active BudgetDTO for the user.
     */
    BudgetDTO getActiveBudget(Long userId);

    /**
     * Retrieve a specific budget.
     *
     * @param budgetId The ID of the budget.
     * @return The BudgetDTO for the specified budget.
     */
    BudgetDTO getBudget(Long budgetId);

    /**
     * Retrieve all budget posts for a specific budget.
     *
     * @param budgetId The ID of the budget.
     * @return A list of BudgetPostDTOs for the specified budget.
     */
    List<BudgetPostDTO> getBudgetPosts(Long budgetId);

    /**
     * Add a budget for a specific user.
     *
     * @param userId    The ID of the user.
     * @param budgetDTO The BudgetDTO to be added.
     */
    void addBudget(Long userId, BudgetDTO budgetDTO);

    /**
     * Delete a specific budget.
     *
     * @param budgetId The ID of the budget to be deleted.
     */
    void deleteBudget(Long budgetId);

    /**
     * Add a budget post to a specific budget.
     *
     * @param budgetId        The ID of the budget.
     * @param budgetPostDTO   The BudgetPostDTO to be added.
     */
    void addBudgetPost(Long budgetId, BudgetPostDTO budgetPostDTO);

    /**
     * Update a specific budget post.
     *
     * @param budgetPostId    The ID of the budget post to be updated.
     * @param budgetPostDTO   The BudgetPostDTO with the updated data.
     */
    void updateBudgetPost(Long budgetPostId, BudgetPostDTO budgetPostDTO);

    /**
     * Delete a specific budget post.
     *
     * @param budgetPostId The ID of the budget post to be deleted.
     * @param category     The category of the budget post to be deleted.
     */
    void deleteBudgetPost(Long budgetPostId, Category category);

    /**
     * Retrieve a budget for a specific user, month, and year.
     *
     * @param userId The ID of the user.
     * @param month  The month of the budget.
     * @param year   The year of the budget.
     * @return The BudgetDTO for the specified user, month, and year.
     */
    BudgetDTO getBudgetByMonthAndYear(Long userId, int month, int year);

    /**
     * Create a budget for a specific user.
     *
     * @param userId The ID of the user.
     * @return The created BudgetDTO.
     */
    BudgetDTO createBudget(Long userId);
}