package org.savingapp.service;

import lombok.AllArgsConstructor;
import org.savingapp.dto.AuthRequestDTO;
import org.savingapp.dto.LoginResponseDTO;
import org.savingapp.dto.UserDTO;
import org.savingapp.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.savingapp.repository.UserRepository;

import java.time.LocalDateTime;


/**
 * Implementation of the AuthenticationService.
 */
@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final RefreshTokenServiceImpl refreshTokenServiceImpl;
    private final JwtServiceImpl jwtServiceImpl;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;


    /**
     * Registers a new user with the specified user DTO.
     * @param userDTO The user DTO.
     * @throws IllegalArgumentException If the username already exists.
     */
    @Override
    public void register(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        userRepository.save(User.builder()
                .fullName(userDTO.getFullName())
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .createdAt(LocalDateTime.now())
                .build());
    }


    /**
     * Logs in a user with the specified user DTO and returns a token DTO.
     * @param authRequestDTO The auth user DTO.
     * @return The token DTO.
     * @throws IllegalArgumentException If the user does not exist.
     */
    @Override
    public LoginResponseDTO login(AuthRequestDTO authRequestDTO) {
        if (userRepository.findByUsername(authRequestDTO.getUsername()).isEmpty()) {
            throw new IllegalArgumentException("User does not exist");
        }

        SecurityContextHolder.getContext().setAuthentication(
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(),
                                authRequestDTO.getPassword())
                )
        );
        return LoginResponseDTO.builder()
                .token(jwtServiceImpl.generateToken(authRequestDTO.getUsername()))
                .refreshToken(refreshTokenServiceImpl.createRefreshToken(authRequestDTO.getUsername()).getToken())
                .fullName(userRepository.findByUsername(authRequestDTO.getUsername()).get().getFullName())
                .build();
    }
}

