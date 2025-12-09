package com.ziminpro.ums.controller;

import com.ziminpro.ums.model.User;
import com.ziminpro.ums.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginSuccessController {

    @Autowired
    private UserService userService;

    @GetMapping("/loginSuccess")
    public Map<String, Object> loginSuccess(OAuth2User oAuthUser) {

        // get username/email from GitHub
        String username = oAuthUser.getAttribute("login");
        if (username == null) {
            username = oAuthUser.getAttribute("email");
        }

        // create or load this user
        User user = userService.findByUsername(username);
        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setPassword("OAUTH"); // placeholder, not used
            userService.saveUser(user);
        }

        // generate your own token
        String token = userService.generateToken(user);

        return Map.of(
                "message", "Login successful",
                "token", token,
                "username", username
        );
    }
}
