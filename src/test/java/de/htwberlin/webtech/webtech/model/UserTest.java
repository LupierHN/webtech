package de.htwberlin.webtech.webtech.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserCreation() {
        User user = new User(1, "testuser", "testuser@example.com", "password", "John", "Doe", null);
        assertNotNull(user);
        assertEquals(1, user.getUId());
        assertEquals("testuser", user.getUsername());
        assertEquals("testuser@example.com", user.getEmail());
    }

    @Test
    public void testUserSetterGetter() {
        User user = new User();
        user.setUsername("newuser");
        user.setEmail("newuser@example.com");
        assertEquals("newuser", user.getUsername());
        assertEquals("newuser@example.com", user.getEmail());
    }
}