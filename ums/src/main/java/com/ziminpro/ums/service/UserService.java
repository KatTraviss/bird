package com.ziminpro.ums.service;

import com.ziminpro.ums.dtos.User;
import com.ziminpro.ums.dtos.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
public class UserService {

    // Map to store users by email/name
    private final Map<String, User> users = new HashMap<>();

    // Map to store UUID tokens and expiry
    private final Map<String, TokenInfo> tokens = new HashMap<>();

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Save a new user (local login)
    public void saveUser(User user) {
        // Generate UUID for id if not set
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }

        // Hash the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save user in map (key = name)
        users.put(user.getName(), user);
    }

    // Find user by username (name)
    public User findByUsername(String username) {
        return users.get(username);
    }

    // Check credentials (local login)
    public boolean checkCredentials(String username, String rawPassword) {
        User user = users.get(username);
        return user != null && passwordEncoder.matches(rawPassword, user.getPassword());
    }

    // -------------------- OAuth2 / Token Methods --------------------

    // Generate a UUID token for a user
    public String generateToken(User user) {
        String token = UUID.randomUUID().toString();
        Instant expiry = Instant.now().plus(Duration.ofMinutes(15));
        tokens.put(token, new TokenInfo(user.getName(), expiry));
        return token;
    }

    // Validate a token and return associated username if valid
    public String validateToken(String token) {
        TokenInfo info = tokens.get(token);
        if (info == null) return null;

        if (Instant.now().isAfter(info.getExpiry())) {
            tokens.remove(token);
            return null;
        }

        return info.getUsername();
    }

    // Revoke a token (logout)
    public void revokeToken(String token) {
        tokens.remove(token);
    }

    // Add a role to a user
    public void addRole(User user, Roles role) {
        if (user != null) {
            user.addRole(role);
        }
    }

    // Inner class to store token info
    private static class TokenInfo {
        private final String username;
        private final Instant expiry;

        public TokenInfo(String username, Instant expiry) {
            this.username = username;
            this.expiry = expiry;
        }

        public String getUsername() {
            return username;
        }

        public Instant getExpiry() {
            return expiry;
        }
    }
}
