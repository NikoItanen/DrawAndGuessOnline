package com.nijoat.backend;

import com.nijoat.backend.model.User;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;



@RestController
@Component
@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
public class Authentication {

    private final Map<String, String> users = new HashMap<>();

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        System.out.println("Received registration request for user: " + username);

        if (users.containsKey(username)) {
            return "User already exists";
        }
        users.put(username, password);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        if (!users.containsKey(username) || !users.get(username).equals(password)) {
            return "Invalid credentials";
        }
        return "Login successful";
    }

}
