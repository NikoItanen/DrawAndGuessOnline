package com.nijoat.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.nijoat.backend.model.User;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    boolean existsByUsername(String username);
    User findByUsername(String username);
    List<User> findTop10ByOrderByPointsDesc();
}
