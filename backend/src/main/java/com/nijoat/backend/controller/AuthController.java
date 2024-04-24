package com.nijoat.backend.controller;

import com.nijoat.backend.model.User;
import com.nijoat.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// Määritellään luokka REST-kontrolleriksi, joka käsittelee HTTP-pyyntöjä.
@RestController
@Component
@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
public class AuthController {

    // Luokan attribuutit, jotka sisältävät viitteet käyttäjärepositoryyn ja salasanojen käsittelyyn käytettävään työkaluun.
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Konstruktori, jossa Spring injektoi UserRepository- ja BCryptPasswordEncoder-riippuvuudet.
    @Autowired
    public AuthController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Käsittelijä rekisteröintipyyntöihin. Vastaanottaa käyttäjän JSON-muodossa, rekisteröi hänet ja tallentaa tietokantaan.
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        System.out.println("Received registration request for user: " + username);

        // Tarkistetaan, onko käyttäjänimi jo olemassa.
        if (userRepository.existsByUsername(username)) {
            return ResponseEntity.badRequest().body("Username already exists!");
        }

        // Salasanan hash-aus ennen tietokantaan tallentamista.
        String hashedPassword = passwordEncoder.encode(password);
        user.setPassword(hashedPassword);
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    // Käsittelijä kirjautumispyyntöihin. Tarkistaa käyttäjätiedot ja palauttaa tilan onnistumisesta.
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();

        // Tarkistetaan, ettei käyttäjänimi tai salasana ole tyhjä.
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username or password cannot be empty");
        }

        // Etsitään käyttäjä tietokannasta.
        User existingUser = userRepository.findByUsername(username);

        // Jos käyttäjää ei löydy, palautetaan virheilmoitus.
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        // Tarkistetaan, täsmääkö annettu salasana tietokannassa olevaan hashattuun salasanaan.
        if (!passwordEncoder.matches(password, existingUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        // Kirjautuminen onnistui.
        return ResponseEntity.ok("Login Successful!");
    }
}
