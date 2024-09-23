package org.savingapp.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RefreshTokenTest {
    private RefreshToken refreshToken;

    @BeforeEach
    public void setup() {
        refreshToken = new RefreshToken();
    }

    @Test
    public void testId() {
        Long idValue = 1L;
        refreshToken.setId(idValue);
        assertEquals(idValue, refreshToken.getId());
    }

    @Test
    public void testToken() {
        String token = "testToken";
        refreshToken.setToken(token);
        assertEquals(token, refreshToken.getToken());
    }

    @Test
    public void testHardExpiry() {
        Instant hardExpiry = Instant.now();
        refreshToken.setHardExpiry(hardExpiry);
        assertEquals(hardExpiry, refreshToken.getHardExpiry());
    }

    @Test
    public void testTickingExpiry() {
        Instant tickingExpiry = Instant.now();
        refreshToken.setTickingExpiry(tickingExpiry);
        assertEquals(tickingExpiry, refreshToken.getTickingExpiry());
    }

    @Test
    public void testUser() {
        User user = new User();
        refreshToken.setUser(user);
        assertEquals(user, refreshToken.getUser());
    }
}
