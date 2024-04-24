package com.nijoat.backend;

import com.nijoat.backend.controller.AuthController;
import com.nijoat.backend.model.User;
import com.nijoat.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthController.class)
class BackendApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void contextLoads() {
    }

    @Test
    void testRegistrationSuccessful() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        String jsonContent = objectMapper.writeValueAsString(user);

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully!"));
    }

    @Test
    void testRegistrationFailureAlreadyExists() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        String jsonContent = objectMapper.writeValueAsString(user);

        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username already exists!"));
    }

 
    @Test
    void testLoginFailureUserNotFound() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        String jsonContent = objectMapper.writeValueAsString(user);

        when(userRepository.findByUsername("testuser")).thenReturn(null);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("User not found"));
    }

    @Test
    void testLoginFailureInvalidCredentials() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        String jsonContent = objectMapper.writeValueAsString(user);

        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(false);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }
}
