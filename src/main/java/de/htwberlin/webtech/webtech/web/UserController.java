package de.htwberlin.webtech.webtech.web;

import de.htwberlin.webtech.webtech.model.User;
import de.htwberlin.webtech.webtech.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/api/auth/")
public class UserController {
    private final UserService userService;

    @GetMapping("/find")
    public ResponseEntity<Boolean> findUser(@RequestBody final String username) {
        final boolean exists = userService.findUser(username);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody final User user) {
        try {
            final User created = userService.registerUser(user);
            String userToken = userService.generateToken(created);
            return new ResponseEntity<>(userToken, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody final User user) {
        try {
            final User found = userService.loginUser(user);
            String userToken = userService.generateToken(found);
            return new ResponseEntity<>(userToken, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/validateToken")
    public ResponseEntity<Boolean> validateToken(@RequestBody final String token) {
        final boolean valid = userService.validateToken(token);
        return new ResponseEntity<>(valid, HttpStatus.OK);
    }

    @PostMapping("/renewToken")
    public ResponseEntity<String> renewToken(@RequestBody final String token) {
        final String newToken = userService.renewToken(token);
        return new ResponseEntity<>(newToken, HttpStatus.OK);
    }

}
