package org.savingapp.repository;

import org.savingapp.model.SavingsGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


/**
 * Repository for the SavingsGoal entity.
 */
public interface SavingsGoalRepository extends JpaRepository<SavingsGoal, Long> {
    Optional<SavingsGoal> findByUserId(Long id);
}
