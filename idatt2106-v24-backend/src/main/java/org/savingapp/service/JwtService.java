package org.savingapp.service;

import io.jsonwebtoken.Claims;

import java.util.function.Function;


/**
 * Interface for the JwtService.
 */
public interface JwtService {

    /**
     * Extract username from the specified JWT.
     *
     * @param token The JWT.
     * @return The username contained in the JWT.
     */
    String extractUsername(String token);

    /**
     * Extract a specific claim from the specified JWT.
     *
     * @param token          The JWT.
     * @param <T>            The type of claim to be extracted.
     * @param claimsResolver The specified claims.
     * @return The extracted claim.
     */
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    /**
     * Extract all claims from the specified JWT.
     *
     * @param token The JWT.
     * @return All claims contained within the token.
     */
    boolean validateToken(String token);

    /**
     * Generate a JWT for the specified username.
     *
     * @param username The username.
     * @return The generated JWT.
     */
    String generateToken(String username);
}
