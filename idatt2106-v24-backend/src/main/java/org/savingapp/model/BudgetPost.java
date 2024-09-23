package org.savingapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.savingapp.enums.Category;

/**
 * Represents a budget post within a budget.
 */
@Entity
@Data
public class BudgetPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "budget_id")
    private Budget budget;

    private Category category;
    private double budgetedSum;

}
