package org.savingapp.repository;

import org.savingapp.model.SavingsGoal;
import org.savingapp.model.SavingsPath;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


/**
 * Repository for the SavingsPath entity.
 */
public interface SavingsPathRepository extends JpaRepository<SavingsPath, Long> {
    Optional<SavingsPath> findBySavingsGoal(SavingsGoal savingsGoal);
}
