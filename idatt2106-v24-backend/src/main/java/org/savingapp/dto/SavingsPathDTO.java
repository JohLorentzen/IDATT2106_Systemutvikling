package org.savingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.savingapp.model.CompletedUserChallenge;
import org.savingapp.model.SavingsGoal;

import java.util.List;

/**
 * Represents the data needed for visualizing the savings path, as a data transfer object.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SavingsPathDTO {
    private SavingsGoalDTO savingsGoal;
    private List<CompletedUserChallengeDTO> completedChallenges;
}
