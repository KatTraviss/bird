package com.ziminpro.ums.security;

import com.ziminpro.ums.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/", "/auth/register", "/auth/login", "/oauth2/**").permitAll()
                    .anyRequest().authenticated()
            )
            .oauth2Login(oauth -> oauth
                    .userInfoEndpoint(userInfo -> userInfo
                            .userService(oauthUserService())
                    )
                    .defaultSuccessUrl("/loginSuccess", true)
            );

        return http.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauthUserService() {
        return request -> {
            DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
            OAuth2User oauth2User = delegate.loadUser(request);

            // Extract email and name from OAuth provider
            String email = oauth2User.getAttribute("email");
            String name = oauth2User.getAttribute("name");

            // Create/find user in  service
            com.ziminpro.ums.dtos.User user = userService.findByEmail(email);
            if (user == null) {
                user = new com.ziminpro.ums.dtos.User();
                user.setEmail(email);
                user.setName(name);
                userService.saveUser(user);
            }

           
            userService.generateToken(user);

            return oauth2User;
        };
    }
}
