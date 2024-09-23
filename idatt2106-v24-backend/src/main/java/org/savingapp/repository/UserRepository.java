package org.savingapp.repository;

import org.savingapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Represents a User repository in the quiz bank application.
 * Provides a custom method to find a user by their username.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their username.
     *
     * @param username The username of the user to be found.
     * @return An Optional that contains the User if found, or empty if not found.
     */
    Optional<User> findByUsername(String username);
}

