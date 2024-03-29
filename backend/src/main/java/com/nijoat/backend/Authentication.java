package com.nijoat.backend;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
public class Authentication {
    
    private Map<String, String> users = new HashMap<>();

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        String nickname = user.getNickname();
        String password = user.getPassword();
        if (users.containsKey(nickname)) {
            return "User already exists";
        }
        users.put(nickname, password);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        String nickname = user.getNickname();
        String password = user.getPassword();
        if (!users.containsKey(nickname) || !users.get(nickname).equals(password)) {
            return "Invalid credentials";
        }
        return "Login successful";
    }
    
    public static class User {
        private String nickname;
        private String password;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
