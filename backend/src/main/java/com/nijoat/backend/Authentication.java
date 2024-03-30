package com.nijoat.backend;

import com.nijoat.backend.model.User;

import com.nijoat.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@RestController
@Component
@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
public class Authentication {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public Authentication(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        System.out.println("Received registration request for user: " + username);

        if (userRepository.existsByUsername(username)) {
            return ResponseEntity.badRequest().body("Username already exists!");
        }

        String hashedPassword = passwordEncoder.encode(password);
        user.setPassword(hashedPassword);
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();

        User existingUser = userRepository.findByUsername(username);

        if (existingUser == null || !passwordEncoder.matches(password, existingUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");

        }
        return ResponseEntity.ok("Login Successful!");
    }
}
