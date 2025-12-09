package com.ziminpro.ums.service;

import com.ziminpro.ums.dao.UmsRepository;
import com.ziminpro.ums.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UmsRepository repo;

    @Override
    public User findByUsername(String username) {
        return repo.findAllUsers()
                .values()
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean checkCredentials(String username, String password) {
        User u = findByUsername(username);
        return u != null && u.getPassword().equals(password);
    }

    @Override
    public void saveUser(User user) {
        repo.createUser(user.toDto());   // you already have DTO conversion
    }

    @Override
    public String generateToken(User user) {
        return UUID.randomUUID().toString();
    }
}
