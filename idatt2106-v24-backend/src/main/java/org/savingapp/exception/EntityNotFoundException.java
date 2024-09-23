package org.savingapp.exception;


/**
 * Exception thrown when an entity is not found in the database.
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Long id, Class<?> entity) {
        super("The " + entity.getSimpleName().toLowerCase() + " with id " + id + " does not exist in our records");
    }

    public EntityNotFoundException(Class<?> entity) {
        super("The " + entity.getSimpleName().toLowerCase() + " does not exist in our records");
    }
}
