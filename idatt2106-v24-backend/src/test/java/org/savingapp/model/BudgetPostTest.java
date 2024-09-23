package org.savingapp.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.savingapp.enums.Category;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BudgetPostTest {
    private BudgetPost budgetPost;

    @BeforeEach
    public void setup() {
        budgetPost = new BudgetPost();
    }

    @Test
    public void testId() {
        Long idValue = 1L;
        budgetPost.setId(idValue);
        assertEquals(idValue, budgetPost.getId());
    }

    @Test
    public void testCategory() {
        Category category = Category.FOOD_AND_DRINKS;
        budgetPost.setCategory(category);
        assertEquals(category, budgetPost.getCategory());
    }

    @Test
    public void testBudgetedSum() {
        Double budgetedSum = 500.0;
        budgetPost.setBudgetedSum(budgetedSum);
        assertEquals(budgetedSum, budgetPost.getBudgetedSum());
    }

    @Test
    public void testBudget() {
        Budget budget = new Budget();
        budgetPost.setBudget(budget);
        assertEquals(budget, budgetPost.getBudget());
    }
}
