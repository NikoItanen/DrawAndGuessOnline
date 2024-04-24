package com.nijoat.backend.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Setter
@Getter
@NoArgsConstructor  // Lombok annotation to generate a no-argument constructor
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private int points;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.points = 0;
    }

    public void incrementPoints() {
        this.points++;
    }
}
