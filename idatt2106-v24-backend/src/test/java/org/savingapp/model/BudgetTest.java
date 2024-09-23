package org.savingapp.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Month;
import java.time.Year;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BudgetTest {
    private Budget budget;

    @BeforeEach
    public void setup() {
        budget = new Budget();
    }

    @Test
    public void testId() {
        Long idValue = 1L;
        budget.setId(idValue);
        assertEquals(idValue, budget.getId());
    }

    @Test
    public void testUser() {
        User user = new User();
        budget.setUser(user);
        assertEquals(user, budget.getUser());
    }

    @Test
    public void testBudgetMonth() {
        Month month = Month.JANUARY;
        budget.setBudgetMonth(month);
        assertEquals(month, budget.getBudgetMonth());
    }

    @Test
    public void testBudgetYear() {
        Year year = Year.of(2022);
        budget.setBudgetYear(year);
        assertEquals(year, budget.getBudgetYear());
    }

    @Test
    public void testBudgetPosts() {
        ArrayList<BudgetPost> budgetPosts = new ArrayList<>();
        budget.setBudgetPosts(budgetPosts);
        assertEquals(budgetPosts, budget.getBudgetPosts());
    }
}
