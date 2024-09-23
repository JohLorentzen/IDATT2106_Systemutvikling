package org.savingapp.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {
    private User user;

    @BeforeEach
    public void setup() {
        user = new User();
    }

    @Test
    public void testId() {
        Long idValue = 1L;
        user.setId(idValue);
        assertEquals(idValue, user.getId());
    }

    @Test
    public void testFullName() {
        String fullName = "Test User";
        user.setFullName(fullName);
        assertEquals(fullName, user.getFullName());
    }

    @Test
    public void testUsername() {
        String username = "testuser";
        user.setUsername(username);
        assertEquals(username, user.getUsername());
    }

    @Test
    public void testPassword() {
        String password = "password";
        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    @Test
    public void testCreatedAt() {
        LocalDateTime createdAt = LocalDateTime.now();
        user.setCreatedAt(createdAt);
        assertEquals(createdAt, user.getCreatedAt());
    }

    @Test
    public void testAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>(user.getAuthorities());
        assertEquals("ROLE_USER", authorities.getFirst().getAuthority());
    }

    @Test
    public void testAccountNonExpired() {
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    public void testAccountNonLocked() {
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    public void testCredentialsNonExpired() {
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    public void testEnabled() {
        assertTrue(user.isEnabled());
    }
}
