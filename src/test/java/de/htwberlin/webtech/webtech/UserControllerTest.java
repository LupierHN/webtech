package de.htwberlin.webtech.webtech;

import de.htwberlin.webtech.webtech.model.Token;
import de.htwberlin.webtech.webtech.model.User;
import de.htwberlin.webtech.webtech.service.NotificationService;
import de.htwberlin.webtech.webtech.service.UserService;
import de.htwberlin.webtech.webtech.utils.TokenUtility;
import de.htwberlin.webtech.webtech.web.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private NotificationService notificationService;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("Test for registering a new user - Expected status: 201 Created")
    public void testRegisterUser() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        Mockito.when(userService.findUser(anyString())).thenReturn(false);
        Mockito.when(userService.registerUser(any(User.class))).thenReturn(user);

        try (MockedStatic<TokenUtility> mockedTokenUtility = Mockito.mockStatic(TokenUtility.class)) {
            mockedTokenUtility.when(() -> TokenUtility.generateAccessToken(any(User.class))).thenReturn(new Token("accessToken"));
            mockedTokenUtility.when(() -> TokenUtility.generateRefreshToken(any(User.class))).thenReturn(new Token("refreshToken"));

            mockMvc.perform(post("/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"username\":\"testuser\",\"password\":\"password\"}"))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$[0].token").value("accessToken"))
                    .andExpect(jsonPath("$[1].token").value("refreshToken"));
        }
    }

    @Test
    @DisplayName("Test for registering an existing user - Expected status: 400 Bad Request")
    public void testRegisterExistingUser() throws Exception {
        Mockito.when(userService.findUser(anyString())).thenReturn(true);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test for logging in a user - Expected status: 200 OK")
    public void testLoginUser() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        Mockito.when(userService.loginUser(any(User.class))).thenReturn(user);

        try (MockedStatic<TokenUtility> mockedTokenUtility = Mockito.mockStatic(TokenUtility.class)) {
            mockedTokenUtility.when(() -> TokenUtility.generateAccessToken(any(User.class))).thenReturn(new Token("accessToken"));
            mockedTokenUtility.when(() -> TokenUtility.generateRefreshToken(any(User.class))).thenReturn(new Token("refreshToken"));

            mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"username\":\"testuser\",\"password\":\"password\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].token").value("accessToken"))
                    .andExpect(jsonPath("$[1].token").value("refreshToken"));
        }
    }

    @Test
    @DisplayName("Test for logging in a non-existing user - Expected status: 404 Not Found")
    public void testLoginNonExistingUser() throws Exception {
        Mockito.when(userService.loginUser(any(User.class))).thenReturn(null);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("Test for finding an existing user by username - Expected status: 200 OK")
    public void testFindUser() throws Exception {
        Mockito.when(userService.findUser(anyString())).thenReturn(true);

        mockMvc.perform(get("/api/auth/find/testuser")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test for finding a non-existing user by username - Expected status: 200 OK")
    public void testFindNonExistingUser() throws Exception {
        Mockito.when(userService.findUser(anyString())).thenReturn(false);

        mockMvc.perform(get("/api/auth/find/testuser")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test for finding an existing user by email - Expected status: 200 OK")
    public void testFindUserE() throws Exception {
        Mockito.when(userService.findUserE(anyString())).thenReturn(true);

        mockMvc.perform(get("/api/auth/finde/testuser@example.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test for finding a non-existing user by email - Expected status: 200 OK")
    public void testFindNonExistingUserE() throws Exception {
        Mockito.when(userService.findUserE(anyString())).thenReturn(false);

        mockMvc.perform(get("/api/auth/finde/testuser@example.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}