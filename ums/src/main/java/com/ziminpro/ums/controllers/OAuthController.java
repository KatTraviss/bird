package com.ziminpro.ums.controllers;

import com.ziminpro.ums.model.User;
import com.ziminpro.ums.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/loginSuccess")
    public String loginSuccess(OAuth2User oAuth2User) {

        String username = oAuth2User.getAttribute("login");
        String email = oAuth2User.getAttribute("email");

        if (username == null) {
            return "Login failed: GitHub did not return username.";
        }

        User existing = userService.findByUsername(username);

        if (existing == null) {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            userService.saveUser(user);
        }

        User finalUser = userService.findByUsername(username);
        String token = userService.generateToken(finalUser);

        return "OAuth login successful. Token: " + token;
    }
}
