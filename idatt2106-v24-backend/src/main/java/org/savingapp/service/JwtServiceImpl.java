package org.savingapp.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


/**
 * Implementation of the JwtService.
 */
@Component
public class JwtServiceImpl implements JwtService {

    public static final String SECRET = "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";


    /**
     * Extract username from the specified JWT.
     *
     * @param token The JWT.
     * @return The username contained in the JWT.
     */
    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    /**
     * Extract a specific claim from the specified JWT.
     *
     * @param token          The JWT.
     * @param <T>            The type of claim to be extracted.
     * @param claimsResolver The specified claims.
     * @return The extracted claim.
     */
    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    /**
     * Extract all claims from the specified JWT.
     *
     * @param token The JWT.
     * @return All claims contained within the token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Checks if the token is valid.
     *
     * @param token The JWT token.
     * @return true if the token is valid, false otherwise.
     */
    @Override
    public boolean validateToken(String token) {
        try {
            return new Date().before(extractClaim(token, Claims::getExpiration));
        } catch (ExpiredJwtException e) {
            return false;
        }
    }


    /**
     * Generate a new JWT.
     *
     * @param username The username the JWT should be linked to.
     * @return The generated token.
     */
    @Override
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }


    /**
     * Build the JWT based on the specified claims and link it to the specified user.
     *
     * @param claims   The claims.
     * @param username The username of the user.
     * @return The new JWT.
     */
    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 120000)) // 2 min
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }


    /**
     * Generate a signing key for the JWT.
     * Decodes the SECRET-constant and then generates a HMAC SHA key from the decoded bytes.
     *
     * @return The generated HMAC SHA key.
     */
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
