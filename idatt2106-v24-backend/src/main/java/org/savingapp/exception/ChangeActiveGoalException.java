package org.savingapp.exception;


/**
 * Exception thrown when trying to change the active goal,
 * but the goal is already active or the end date has already passed.
 */
public class ChangeActiveGoalException extends RuntimeException {
    public ChangeActiveGoalException(Long id) {
        super("The goal with id " + id + " is already active or end date has already passed.");
    }
}
