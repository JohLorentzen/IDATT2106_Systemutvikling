package org.savingapp.service;

import lombok.AllArgsConstructor;
import org.savingapp.dto.RefreshTokenRequestDTO;
import org.savingapp.dto.RefreshTokenResponseDTO;
import org.savingapp.exception.TokenExpiredException;
import org.savingapp.model.RefreshToken;
import org.savingapp.model.User;
import org.savingapp.repository.RefreshTokenRepository;
import org.savingapp.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


/**
 * Implementation of the RefreshTokenService.
 */
@AllArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtServiceImpl jwtServiceImpl;


    /**
     * Refreshes the access token.
     *
     * @param refreshTokenRequestDTO The refresh token request.
     * @return The refresh token response.
     */
    @Override
    public RefreshTokenResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO) {
        return findByToken(refreshTokenRequestDTO.getRefreshToken())
                .map(this::verifyExpiration)
                .map(token -> {
                    token.setTickingExpiry(Instant.now().plusMillis(300000)); // plus 5 min
                    refreshTokenRepository.save(token);
                    return new RefreshTokenResponseDTO(jwtServiceImpl.generateToken(token.getUser().getUsername()));
                }).orElseThrow(() -> new RuntimeException("The refresh token was not found."));
    }


    /**
     * Creates a new refresh token.
     *
     * @param username The username.
     * @return The refresh token.
     */
    @Override
    public RefreshToken createRefreshToken(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Optional<RefreshToken> existingRefreshToken = refreshTokenRepository.findByUser(user);
        RefreshToken refreshToken;

        if (existingRefreshToken.isPresent()) {
            refreshToken = existingRefreshToken.get();
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setHardExpiry(Instant.now().plusMillis(3600000)); // 1 hour
            refreshToken.setTickingExpiry(Instant.now().plusMillis(300000)); // 5 min
        } else {
            refreshToken = RefreshToken.builder()
                    .user(user)
                    .token(UUID.randomUUID().toString())
                    .hardExpiry(Instant.now().plusMillis(3600000)) // 1 hour
                    .tickingExpiry(Instant.now().plusMillis(300000)) // 5 min
                    .build();
        }

        return refreshTokenRepository.save(refreshToken);
    }


    /**
     * Finds a refresh token by its token.
     *
     * @param token The token.
     * @return The refresh token.
     */
    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }


    /**
     * Verifies the expiration of a refresh token.
     *
     * @param token The token.
     * @return The verified token.
     */
    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getHardExpiry().compareTo(Instant.now()) < 0 ||
                token.getTickingExpiry().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenExpiredException("Refresh token is expired. Please log in for renewal.");
        }

        token.setTickingExpiry(Instant.now().plusMillis(600000));

        return token;
    }
}
