package org.savingapp.util;

import lombok.AllArgsConstructor;
import org.savingapp.model.User;
import org.savingapp.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


/**
 * This class provides utility methods for working with User objects in the context of Spring Security.
 * It provides a method for retrieving the currently authenticated user.
 */
@Component
@AllArgsConstructor
public class UserUtil {

    public UserRepository userRepository;

    /**
     * Gets the current user.
     * The current user is read from the UserDetails through the SecurityContextHolder.
     *
     * @return The current user.
     * @throws UsernameNotFoundException If the user is not found.
     */
    public User getCurrentUser() throws UsernameNotFoundException {
        return userRepository.findByUsername(((UserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal())
                        .getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

