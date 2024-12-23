package de.htwberlin.webtech.webtech.web;

import de.htwberlin.webtech.webtech.model.Token;
import de.htwberlin.webtech.webtech.model.User;
import de.htwberlin.webtech.webtech.service.UserService;
import de.htwberlin.webtech.webtech.utils.TokenUtility;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

    //JUST FOR TESTING
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<User>> getUsers() {
        final Iterable<User> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Get a user by id from the Authorization Header
     *
     * @return User
     */
    @GetMapping("/get")
    public ResponseEntity<User> getUser(@RequestHeader("Authorization") String authHeader) {
        if (!TokenUtility.validateAuthHeader(authHeader)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        final User user = TokenUtility.getUserFromHeader(authHeader, userService);
        if (user == null) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok(user);
    }

    /**
     * Check if a user exists
     *
     * @param username String
     * @return Boolean exists
     */
    @GetMapping("/find/{username}")
    public ResponseEntity<Boolean> findUser(@PathVariable final String username) {
        final boolean exists = userService.findUser(username);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }


    /**
     * Check if a user exists
     *
     * @param email String
     * @return Boolean exists
     */
    @GetMapping("/finde/{email}")
    public ResponseEntity<Boolean> findUserE(@PathVariable final String email) {
        final boolean exists = userService.findUserE(email);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

    /**
     * Register a new user
     *
     * @param user User
     * @return Tokens accessToken, refreshToken
     */
    @PostMapping("/register")
    public ResponseEntity<List<Token>> registerUser(@Valid @RequestBody final User user) {
        List<Token> tokens = new ArrayList<>();
        try {
            if (userService.findUser(user.getUsername())) return ResponseEntity.badRequest().build();
            final User created = userService.registerUser(user);
            tokens.add(TokenUtility.generateAccessToken(created));
            tokens.add(TokenUtility.generateRefreshToken(created));
            return new ResponseEntity<>(tokens, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Login a user
     *
     * @param user User with mail and password
     * @return Tokens accessToken, refreshToken
     */
    @PostMapping("/login")
    public ResponseEntity<List<Token>> loginUser(@Valid @RequestBody final User user) {
        List<Token> tokens = new ArrayList<>();
        try {
            final User found = userService.loginUser(user);
            if (found == null) return ResponseEntity.notFound().build();
            tokens.add(TokenUtility.generateAccessToken(found));
            tokens.add(TokenUtility.generateRefreshToken(found));
            return new ResponseEntity<>(tokens, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Check if a token is valid
     *
     * @param token Token
     * @return Boolean valid
     */
    @PostMapping("/validateToken")
    public ResponseEntity<Boolean> validateToken(@RequestBody final Token token) {
        final boolean valid = TokenUtility.validateToken(token);
        return new ResponseEntity<>(valid, HttpStatus.OK);
    }

    /**
     * Renew an access token
     *
     * @param token refreshToken
     * @param authHeader Authorization Header with accessToken
     * @return Token accessToken
     */
    @PostMapping("/renewToken")
    public ResponseEntity<Token> renewToken(@RequestBody final Token token, @RequestHeader("Authorization") String authHeader) {
        Token accessToken;
        try {
            accessToken = new Token(authHeader.substring(7));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final Token newToken = TokenUtility.renewToken(token, accessToken);
        if (newToken == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else return new ResponseEntity<>(newToken, HttpStatus.OK);
    }

    /**
     * update a user
     *
     * @param user User
     * @return User updated
     */
    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@Valid @RequestBody final User user, @RequestHeader("Authorization") String authHeader) {
        if (!TokenUtility.validateAuthHeader(authHeader)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User db_user = TokenUtility.getUserFromHeader(authHeader, userService);
        if (db_user == null) return ResponseEntity.notFound().build();
        user.setUId(db_user.getUId());
        final User updated = userService.updateUser(user);
        if (updated == null) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok(updated);
    }

    /**
     * delete a user
     *
     * @param id int
     * @return void
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") final int id) {
        final boolean removed = userService.deleteUser(id);
        if (removed) return ResponseEntity.noContent().build();
        else return ResponseEntity.notFound().build();
    }

    //JUST FOR TESTING
    @GetMapping("/testToken")
    public ResponseEntity<Token> testToken() {
        Token token = TokenUtility.getTestToken();
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

}
