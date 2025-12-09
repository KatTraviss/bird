package com.ziminpro.ums.security;

import com.ziminpro.ums.model.User;
import com.ziminpro.ums.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    public String authenticate(String username, String password) {
        if (userService.checkCredentials(username, password)) {
            User user = userService.findByUsername(username);
            return jwtUtil.generateToken(user);
        }
        throw new RuntimeException("Invalid username or password");
    }
}
