package com.nijoat.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class BackendServer {

	public static void main(String[] args) {
		SpringApplication.run(BackendServer.class, args);
	}

	@GetMapping("/")
	public String apiRoot() {
		return "Hello World!";
	}
}