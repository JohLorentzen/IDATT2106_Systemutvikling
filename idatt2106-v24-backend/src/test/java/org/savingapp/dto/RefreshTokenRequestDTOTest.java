package org.savingapp.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RefreshTokenRequestDTOTest {
    private RefreshTokenRequestDTO refreshTokenRequestDTO;

    @BeforeEach
    public void setup() {
        refreshTokenRequestDTO = new RefreshTokenRequestDTO();
    }

    @Test
    public void testRefreshToken() {
        String refreshToken = "TestRefreshToken";
        refreshTokenRequestDTO.setRefreshToken(refreshToken);
        assertEquals(refreshToken, refreshTokenRequestDTO.getRefreshToken());
    }
}
