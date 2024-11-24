package com.Hackathon.minutesGamification.services;

import com.Hackathon.minutesGamification.database.entities.User;
import com.Hackathon.minutesGamification.database.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticateService {

    private final UserRepository usersRepository;
    private Map<String, User> users;

    public AuthenticateService(UserRepository usersRepository) {
        this.usersRepository = usersRepository;
        users = new HashMap<>();
    }

    @PostConstruct
    public void init() {

    }

    @Transactional
    public boolean userExists(String userName) {
        for (User user : usersRepository.findAll()) {
            users.put(user.getUsername(), user);
        }
        return users.containsKey(userName);
    }

    public User getUser(String username, String password) {
        if (users.get(username).getPassword().equals(password)) {
            return users.get(username);
        }else {
            return null;
        }
    }

}
