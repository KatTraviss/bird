package com.ziminpro.ums.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;                // unique user ID
    private String username;        // login username
    private String password;        // hashed password ideally
    private String email;           // optional, for OAuth / notifications
    private Set<String> roles;      // e.g., ADMIN, USER

    private String token;           // generated UUID token for OAuth
    private long tokenExpiry;       // epoch seconds, token expiration time

    // * helper method
    public boolean isTokenValid() {
        return token != null && System.currentTimeMillis() / 1000 < tokenExpiry;
    }
}
