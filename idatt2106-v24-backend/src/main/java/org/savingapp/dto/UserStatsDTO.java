package org.savingapp.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Represents user statistics, as a data transfer object.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatsDTO {
    private Long id;
    private double totalSaved;
    private int completedChallenges;
    private int completedGoals;
    private Long userId;
}