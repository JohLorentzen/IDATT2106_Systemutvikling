package org.savingapp.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RefreshTokenResponseDTOTest {
    private RefreshTokenResponseDTO refreshTokenResponseDTO;

    @BeforeEach
    public void setup() {
        refreshTokenResponseDTO = new RefreshTokenResponseDTO();
    }

    @Test
    public void testToken() {
        String token = "TestToken";
        refreshTokenResponseDTO.setToken(token);
        assertEquals(token, refreshTokenResponseDTO.getToken());
    }
}
