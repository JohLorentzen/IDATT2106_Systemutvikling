package org.savingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a budget.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BudgetDTO {
    private Long id;
    private int month;
    private int year;
    private double totalBudgetedSum;
    private double totalSpentSum;
    private List<BudgetPostDTO> budgetPosts;
}
