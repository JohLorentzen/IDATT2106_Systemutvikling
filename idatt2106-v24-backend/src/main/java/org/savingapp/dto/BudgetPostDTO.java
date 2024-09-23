package org.savingapp.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.savingapp.enums.Category;

/**
 * Represents a budget post within a budget.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BudgetPostDTO {
    private long id;
    private Category category;
    private Integer budgetedSum;
    private Integer actualSum;

}
