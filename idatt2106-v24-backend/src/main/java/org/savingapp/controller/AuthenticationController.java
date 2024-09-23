package org.savingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.savingapp.dto.*;
import org.savingapp.exception.TokenExpiredException;
import org.savingapp.service.AuthenticationService;
import org.savingapp.service.RefreshTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller for the Authentication API.
 */
@AllArgsConstructor
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication API")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;


    /**
     * Handles the request for user registration.
     *
     * @param userDTO UserDTO instance representing the user data.
     * @return ResponseEntity<?> instance representing the HTTP response.
     */
    @PostMapping("/register")
    @Operation(summary = "Register",
            description = "Register with username and password",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User registered"),
                    @ApiResponse(responseCode = "409", description = "Username already exists"),
                    @ApiResponse(responseCode = "500", description = "Unknown error occurred")
            })
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        try {
            authenticationService.register(userDTO);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Handles the request for user login.
     *
     * @param authRequestDTO AuthRequestDTO instance representing the user's login credentials.
     * @return ResponseEntity<LoginResponseDTO> instance representing the HTTP response.
     */
    @PostMapping("/login")
    @Operation(summary = "Login",
            description = "Login with username and password",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User logged in"),
                    @ApiResponse(responseCode = "401", description = "Bad credentials"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "500", description = "Unknown error occurred")
            })
    public ResponseEntity<LoginResponseDTO> login(@RequestBody AuthRequestDTO authRequestDTO) {

        try {
            return ResponseEntity.status(HttpStatus.OK).body(authenticationService.login(authRequestDTO));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Handles the refreshing of the access token, using the refresh token provided from register/login.
     *
     * @param refreshTokenRequestDTO RefreshTokenRequestDTO representing the refresh token.
     * @return ResponseEntity<RefreshTokenResponseDTO> instance representing the HTTP response.
     */
    @PostMapping("/refresh")
    @Operation(summary = "Refresh",
            description = "Refresh access token using refresh token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Access token renewed"),
                    @ApiResponse(responseCode = "403", description = "Refresh token expired"),
                    @ApiResponse(responseCode = "500", description = "Unknown error occurred")
            })
    public ResponseEntity<RefreshTokenResponseDTO> refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(refreshTokenService.refreshToken(refreshTokenRequestDTO));
        } catch (TokenExpiredException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}