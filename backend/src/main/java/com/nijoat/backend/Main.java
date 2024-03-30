package com.nijoat.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@GetMapping("/")
	public String apiRoot() {
		return "Hello World!";
	}
}
