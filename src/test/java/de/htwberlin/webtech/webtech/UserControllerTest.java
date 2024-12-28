package de.htwberlin.webtech.webtech;

import de.htwberlin.webtech.webtech.model.Token;
import de.htwberlin.webtech.webtech.model.User;
import de.htwberlin.webtech.webtech.service.UserService;
import de.htwberlin.webtech.webtech.utils.TokenUtility;
import de.htwberlin.webtech.webtech.web.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("Test for registering a user - Expected status: 201 Created")
    public void testRegisterUser() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("testmail@test.com");
        user.setFirstName("Test");
        user.setLastName("User");

        Mockito.when(userService.registerUser(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(userService.findUser("testuser")).thenReturn(Boolean.FALSE);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"testuser\",\"password\":\"password\",\"email\":\"testmail@test.com\",\"firstName\":\"Test\",\"lastName\":\"User\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Test for registering a user with an existing username - Expected status: 400 Bad Request")
    public void testRegisterUserWithExistingUsername() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("testmail@test.com");
        user.setFirstName("Test");
        user.setLastName("User");

        Mockito.when(userService.findUser("testuser")).thenReturn(Boolean.TRUE);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"testuser\",\"password\":\"password\",\"email\":\"testmail@test.com\",\"firstName\":\"Test\",\"lastName\":\"User\"}"))
                .andExpect(status().isBadRequest());
    }



    @Test
    @DisplayName("Test for logging in a user - Expected status: 200 OK")
    public void testLoginUser() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        Mockito.when(userService.loginUser(Mockito.any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test for getting all users - Expected status: 200 OK")
    public void testGetUsers() throws Exception {
        mockMvc.perform(get("/api/auth")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test for finding a user by username - Expected status: 200 OK, Expected content: 'true'")
    public void testFindUser() throws Exception {
        Mockito.when(userService.findUser("testuser")).thenReturn(Boolean.TRUE);

        mockMvc.perform(get("/api/auth/find/testuser")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("Test for validating a token - Expected status: 200 OK")
    public void testValidateToken() throws Exception {
        mockMvc.perform(post("/api/auth/validateToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"token\":\"testtoken\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test for renewing a token - Expected status: 200 OK")
    public void testRenewToken() throws Exception {
        Token refreshToken = new Token("testRefreshToken");
        Token newAccessToken = new Token("newAccessToken");

        Mockito.mockStatic(TokenUtility.class);
        Mockito.when(TokenUtility.renewToken(Mockito.any(Token.class), Mockito.any(Token.class))).thenReturn(newAccessToken);

        mockMvc.perform(post("/api/auth/renewToken")
                .header("Authorization", "Bearer testAccessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"token\":\"testRefreshToken\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("newAccessToken"));
    }

    @Test
    @DisplayName("Test for updating a user - Expected status: 200 OK")
    public void testUpdateUser() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        Mockito.when(userService.updateUser(Mockito.any(User.class))).thenReturn(user);

        mockMvc.perform(put("/api/auth/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test for deleting a user - Expected status: 204 No Content")
    public void testDeleteUser() throws Exception {
        Mockito.when(userService.deleteUser(1)).thenReturn(Boolean.TRUE);

        mockMvc.perform(delete("/api/auth/delete/1"))
                .andExpect(status().isNoContent());
    }
}