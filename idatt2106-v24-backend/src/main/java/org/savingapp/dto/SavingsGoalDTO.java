package org.savingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


/**
 * Represents a savings goal, as a data transfer object.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavingsGoalDTO {
    private Long id;
    private String title;
    private double goalAmount;
    private double savedAmount;
    private LocalDate endDate;
    private boolean isActive;
    private long daysLeft = getDaysLeft();

    public long getDaysLeft() {
        LocalDate today = LocalDate.now();
        if (endDate == null || today.isAfter(endDate)) return 0;
        return endDate.toEpochDay() - today.toEpochDay();
    }
}
