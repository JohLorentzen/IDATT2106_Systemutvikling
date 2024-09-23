package org.savingapp.service;

import org.savingapp.dto.RefreshTokenRequestDTO;
import org.savingapp.dto.RefreshTokenResponseDTO;
import org.savingapp.model.RefreshToken;

import java.util.Optional;


/**
 * Interface for the RefreshTokenService.
 */
public interface RefreshTokenService {

    /**
     * Refreshes the access token.
     *
     * @param refreshTokenRequestDTO The refresh token request.
     * @return The refresh token response.
     */
    RefreshTokenResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO);

    /**
     * Creates a new refresh token.
     *
     * @param username The username.
     * @return The refresh token.
     */
    RefreshToken createRefreshToken(String username);

    /**
     * Finds a refresh token by the token.
     *
     * @param token The token.
     * @return The refresh token.
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Verifies the expiration of a refresh token.
     *
     * @param token The refresh token.
     * @return The refresh token.
     */
    RefreshToken verifyExpiration(RefreshToken token);

}
