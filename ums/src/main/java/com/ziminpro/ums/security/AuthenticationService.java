package com.ziminpro.ums.security;

import com.ziminpro.ums.model.User;
import com.ziminpro.ums.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthenticationService {

    @Autowired
    private UserService userService;

    public UUID authenticate(String username, String password) {

        // Check credentials using  UserService
        if (!userService.checkCredentials(username, password)) {
            throw new RuntimeException("Invalid username or password");
        }

        User user = userService.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Generate a simple session token
        return UUID.randomUUID();
    }
}
