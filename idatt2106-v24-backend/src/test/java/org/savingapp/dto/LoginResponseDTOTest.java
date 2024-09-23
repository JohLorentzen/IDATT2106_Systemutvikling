package org.savingapp.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginResponseDTOTest {
    private LoginResponseDTO loginResponseDTO;

    @BeforeEach
    public void setup() {
        loginResponseDTO = new LoginResponseDTO();
    }

    @Test
    public void testToken() {
        String token = "TestToken";
        loginResponseDTO.setToken(token);
        assertEquals(token, loginResponseDTO.getToken());
    }

    @Test
    public void testRefreshToken() {
        String refreshToken = "TestRefreshToken";
        loginResponseDTO.setRefreshToken(refreshToken);
        assertEquals(refreshToken, loginResponseDTO.getRefreshToken());
    }

    @Test
    public void testFullName() {
        String fullName = "Test User";
        loginResponseDTO.setFullName(fullName);
        assertEquals(fullName, loginResponseDTO.getFullName());
    }
}
