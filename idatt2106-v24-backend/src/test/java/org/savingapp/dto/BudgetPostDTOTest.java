package org.savingapp.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.savingapp.enums.Category;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BudgetPostDTOTest {
    private BudgetPostDTO budgetPostDTO;

    @BeforeEach
    public void setup() {
        budgetPostDTO = new BudgetPostDTO();
    }

    @Test
    public void testId() {
        long id = 1L;
        budgetPostDTO.setId(id);
        assertEquals(id, budgetPostDTO.getId());
    }

    @Test
    public void testCategory() {
        Category category = Category.TRANSPORTATION;
        budgetPostDTO.setCategory(category);
        assertEquals(category, budgetPostDTO.getCategory());
    }

    @Test
    public void testBudgetedSum() {
        Integer budgetedSum = 1000;
        budgetPostDTO.setBudgetedSum(budgetedSum);
        assertEquals(budgetedSum, budgetPostDTO.getBudgetedSum());
    }

    @Test
    public void testActualSum() {
        Integer actualSum = 500;
        budgetPostDTO.setActualSum(actualSum);
        assertEquals(actualSum, budgetPostDTO.getActualSum());
    }
}
