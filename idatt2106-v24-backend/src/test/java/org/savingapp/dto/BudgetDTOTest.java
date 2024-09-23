package org.savingapp.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BudgetDTOTest {
    private BudgetDTO budgetDTO;

    @BeforeEach
    public void setup() {
        budgetDTO = new BudgetDTO();
    }

    @Test
    public void testId() {
        Long id = 1L;
        budgetDTO.setId(id);
        assertEquals(id, budgetDTO.getId());
    }

    @Test
    public void testMonth() {
        int month = 1;
        budgetDTO.setMonth(month);
        assertEquals(month, budgetDTO.getMonth());
    }

    @Test
    public void testYear() {
        int year = 2022;
        budgetDTO.setYear(year);
        assertEquals(year, budgetDTO.getYear());
    }

    @Test
    public void testTotalBudgetedSum() {
        double totalBudgetedSum = 1000.0;
        budgetDTO.setTotalBudgetedSum(totalBudgetedSum);
        assertEquals(totalBudgetedSum, budgetDTO.getTotalBudgetedSum());
    }

    @Test
    public void testTotalSpentSum() {
        double totalSpentSum = 500.0;
        budgetDTO.setTotalSpentSum(totalSpentSum);
        assertEquals(totalSpentSum, budgetDTO.getTotalSpentSum());
    }

    @Test
    public void testBudgetPosts() {
        BudgetPostDTO budgetPostDTO = new BudgetPostDTO();
        // Set properties for budgetPostDTO here
        budgetDTO.setBudgetPosts(Collections.singletonList(budgetPostDTO));
        assertEquals(1, budgetDTO.getBudgetPosts().size());
    }
}
