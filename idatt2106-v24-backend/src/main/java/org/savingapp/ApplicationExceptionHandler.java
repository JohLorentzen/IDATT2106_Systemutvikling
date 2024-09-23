package org.savingapp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.savingapp.aspect.LoggingAspect;
import org.savingapp.exception.ChangeActiveGoalException;
import org.savingapp.exception.EntityNotFoundException;
import org.savingapp.exception.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * Global exception handler for the application.
 */
@ControllerAdvice
public class ApplicationExceptionHandler {

    /**
     * Handles EntityNotFoundExceptions.
     *
     * @param ex The exception.
     * @return The response entity.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        Logger logger = LogManager.getLogger(LoggingAspect.class);
        logger.error(getErrorMessage(ex));
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }


    /**
     * Handles ChangeActiveGoalExceptions.
     *
     * @param ex The exception.
     * @return The response entity.
     */
    @ExceptionHandler(ChangeActiveGoalException.class)
    public ResponseEntity<Object> handleChangeActiveGoalException(ChangeActiveGoalException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        Logger logger = LogManager.getLogger(LoggingAspect.class);
        logger.error(getErrorMessage(ex));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    /**
     * Handles DataIntegrityViolationExceptions.
     *
     * @param ex The exception.
     * @return The response entity.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String simplifiedMessage = "Unique index or primary key violation";
        ErrorResponse errorResponse = new ErrorResponse(simplifiedMessage);
        Logger logger = LogManager.getLogger(LoggingAspect.class);
        logger.error(ex.getClass().getSimpleName() + ": " + simplifiedMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    /**
     * Handles InvalidDataAccessApiUsageExceptions.
     *
     * @param ex The exception.
     * @return The response entity.
     */
    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<Object> handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException ex) {
        String simplifiedMessage = "Multiple representations of the same entity";
        if (ex.getRootCause() instanceof ConstraintViolationException) {
            String entity = ((ConstraintViolationException) ex.getRootCause()).getErrorMessage();
            simplifiedMessage += ": " + entity;
        }
        ErrorResponse errorResponse = new ErrorResponse(simplifiedMessage);
        Logger logger = LogManager.getLogger(LoggingAspect.class);
        logger.error(ex.getClass().getSimpleName() + ": " + simplifiedMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }


    /**
     * Handles all other exceptions (RuntimeExceptions).
     *
     * @param ex The exception.
     * @return The response entity.
     */
    private String getErrorMessage(RuntimeException ex) {
        return ex.getClass().getSimpleName() + ": " + ex.getMessage();
    }
}
