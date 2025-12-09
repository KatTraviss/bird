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

        // Get GitHub username
        String email = oAuthUser.getAttribute("email");
        String name = oAuthUser.getAttribute("login");

        if (email == null && name == null) {
            return Map.of("error", "GitHub did not return any usable identity");
        }

        // If GitHub gives only username, build a fake email
        if (email == null) {
            email = name + "@github-user.local";
        }

        // Create or load user
        User user = userService.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setName(name != null ? name : email);
            user.setPassword("OAUTH");
            userService.saveUser(user);
        }

        // Generate token
        String token = userService.generateToken(user);

        return Map.of(
                "message", "OAuth login successful",
                "email", email,
                "token", token
        );
    }
}

