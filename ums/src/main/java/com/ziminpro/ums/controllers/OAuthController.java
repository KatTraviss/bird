package com.ziminpro.ums.controllers;

import com.ziminpro.ums.dtos.User;
import com.ziminpro.ums.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class OAuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/loginSuccess")
    public Map<String, Object> loginSuccess(OAuth2User oAuthUser) {

        // Extract GitHub username/email
        String username = oAuthUser.getAttribute("login");
        if (username == null) {
            username = oAuthUser.getAttribute("email");
        }

        if (username == null) {
            return Map.of("message", "Login failed: GitHub did not return username or email");
        }

        // Check if user exists; create if not
        User user = userService.findByUsername(username);
        if (user == null) {
            user = new User();
            user.setName(username);   // DTO uses 'name'
            user.setEmail(username);  // store email if available
            user.setPassword("OAUTH"); // placeholder for OAuth users
            userService.saveUser(user);
        }

        // Generate UUID token
        String token = userService.generateToken(user);

        return Map.of(
                "message", "OAuth login successful",
                "username", user.getName(),
                "token", token
        );
    }
}
