package com.ziminpro.ums.controllers;

import com.ziminpro.ums.model.User;
import com.ziminpro.ums.service.UserService;
import com.ziminpro.ums.security.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authService;

    // Register new user
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        // Assign default role
        user.setRoles(Set.of("ROLE_USER"));
        userService.saveUser(user);
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    // Login and get JWT
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            String token = authService.authenticate(user.getUsername(), user.getPassword());
            return ResponseEntity.ok(Map.of("token", token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }
}
