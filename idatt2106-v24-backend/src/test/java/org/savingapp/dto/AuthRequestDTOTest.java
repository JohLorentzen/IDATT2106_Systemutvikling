package org.savingapp.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthRequestDTOTest {
    private AuthRequestDTO authRequestDTO;

    @BeforeEach
    public void setup() {
        authRequestDTO = new AuthRequestDTO();
    }

    @Test
    public void testUsername() {
        String username = "TestUser";
        authRequestDTO.setUsername(username);
        assertEquals(username, authRequestDTO.getUsername());
    }

    @Test
    public void testPassword() {
        String password = "TestPassword";
        authRequestDTO.setPassword(password);
        assertEquals(password, authRequestDTO.getPassword());
    }
}
