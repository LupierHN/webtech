package de.htwberlin.webtech.webtech;

import de.htwberlin.webtech.webtech.model.User;
import de.htwberlin.webtech.webtech.persistence.UserRepository;
import de.htwberlin.webtech.webtech.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setUId(1);
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("password");
    }

    @Test
    @DisplayName("Test for registering a new user")
    public void testRegisterUser() {
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);

        User registeredUser = userService.registerUser(user);
        assertNotNull(registeredUser);
        assertEquals(user.getUsername(), registeredUser.getUsername());
        assertTrue(new BCryptPasswordEncoder().matches("password", registeredUser.getPassword()));
    }

    @Test
    @DisplayName("Test for getting all users")
    public void testGetUsers() {
        Mockito.when(userRepository.findAll()).thenReturn(Set.of(user));

        Iterable<User> users = userService.getUsers();
        assertNotNull(users);
        assertTrue(users.iterator().hasNext());
    }

    @Test
    @DisplayName("Test for getting a user by ID")
    public void testGetUserById() {
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        User foundUser = userService.getUser(1);
        assertNotNull(foundUser);
        assertEquals(user.getUId(), foundUser.getUId());
    }

    @Test
    @DisplayName("Test for getting a user by username")
    public void testGetUserByUsername() {
        Mockito.when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        User foundUser = userService.getUserByUsername("testuser");
        assertNotNull(foundUser);
        assertEquals(user.getUsername(), foundUser.getUsername());
    }

    @Test
    @DisplayName("Test for logging in a user")
    public void testLoginUser() {
        User loginUser = new User();
        loginUser.setEmail("testuser@example.com");
        loginUser.setPassword("password");

        user.setPassword(new BCryptPasswordEncoder().encode("password"));
        Mockito.when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        User loggedInUser = userService.loginUser(loginUser);
        assertNotNull(loggedInUser);
        assertEquals(user.getUsername(), loggedInUser.getUsername());
    }

    @Test
    @DisplayName("Test for updating a user")
    public void testUpdateUser() {
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);

        user.setPassword("newpassword");
        User updatedUser = userService.updateUser(user);
        assertNotNull(updatedUser);
        assertTrue(new BCryptPasswordEncoder().matches("newpassword", updatedUser.getPassword()));
    }

    @Test
    @DisplayName("Test for deleting a user")
    public void testDeleteUser() {
        Mockito.when(userRepository.existsById(anyInt())).thenReturn(true);

        boolean isDeleted = userService.deleteUser(1);
        assertTrue(isDeleted);
    }
}