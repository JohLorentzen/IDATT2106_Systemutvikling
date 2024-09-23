package org.savingapp.repository;

import org.savingapp.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Optional;

/**
 * Represents a budget repository that handles persistence of budgets.
 * Extends JpaRepository to provide CRUD operations for the Budget entity.
 * Provides custom methods to find a Budget-object by its user id, month, and year.
 */

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByUserId(Long userId);

    Optional<Budget> findByUserIdAndBudgetMonthAndBudgetYear(Long userId, Month month, Year year);
}
