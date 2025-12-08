package com.ziminpro.ums.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;                   // unique user ID
    private String username;           // login username
    private String password;           // hashed password ideally
    private String email;              // optional, for OpenID Connect / notifications
    private Set<String> roles;         // for RBAC, e.g., ADMIN, USER
}
