package org.savingapp.service;

import org.savingapp.dto.AuthRequestDTO;
import org.savingapp.dto.LoginResponseDTO;
import org.savingapp.dto.UserDTO;


/**
 * Interface for the AuthenticationService.
 */
public interface AuthenticationService {
    /**
     * Registers a new user with the specified user DTO.
     *
     * @param userDTO The user DTO.
     */
    void register(UserDTO userDTO);

    /**
     * Logs in a user with the specified user DTO and returns a token DTO.
     *
     * @param authRequestDTO The auth user DTO.
     * @return The token DTO.
     */
    LoginResponseDTO login(AuthRequestDTO authRequestDTO);
}
