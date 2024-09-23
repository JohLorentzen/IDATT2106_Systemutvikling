package org.savingapp.service;

import org.savingapp.dto.SavingsGoalDTO;
import org.savingapp.model.User;

import java.util.List;


/**
 * Interface for the SavingsGoalService.
 */
public interface SavingsGoalService {
    /**
     * Set a savings goal as active.
     *
     * @param id   The savings goal to set as active.
     * @param user The user to set the savings goal as active for.
     */
    void setActiveSavingsGoal(Long id, User user);

    /**
     * Get the active savings goal for a user.
     *
     * @param user The user to get the active savings goal for.
     * @return The active savings goal.
     */
    SavingsGoalDTO getActiveSavingsGoal(User user);

    List<SavingsGoalDTO> getArchivedSavingGoals(User user);

    List<SavingsGoalDTO> getCurrentSavingGoals(User user);

    SavingsGoalDTO updateSavingsGoal(SavingsGoalDTO goal, User user);

    void archiveSavingsGoal(Long id);
}
