package org.savingapp.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDTOTest {
    private UserDTO userDTO;

    @BeforeEach
    public void setup() {
        userDTO = new UserDTO();
    }

    @Test
    public void testFullName() {
        String fullName = "Test User";
        userDTO.setFullName(fullName);
        assertEquals(fullName, userDTO.getFullName());
    }

    @Test
    public void testUsername() {
        String username = "testuser";
        userDTO.setUsername(username);
        assertEquals(username, userDTO.getUsername());
    }

    @Test
    public void testPassword() {
        String password = "testpassword";
        userDTO.setPassword(password);
        assertEquals(password, userDTO.getPassword());
    }
}
