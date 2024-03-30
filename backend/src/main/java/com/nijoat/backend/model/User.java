package com.nijoat.backend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Setter
@Getter
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String username;
    private String password;

}