package com.nijoat.backend.controller;

import com.nijoat.backend.model.User;
import com.nijoat.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Component
@RequestMapping("/leaderboard")
public class LeaderboardController {
    private final UserRepository userRepository;

    @Autowired
    public LeaderboardController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/top")
    public ResponseEntity<List<User>> getTopPlayers() {
        List<User> topPlayers = userRepository.findTop10ByOrderByPointsDesc();
        return ResponseEntity.ok(topPlayers);
    }
}
