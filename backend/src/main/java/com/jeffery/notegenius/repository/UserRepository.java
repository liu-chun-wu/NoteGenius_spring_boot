package com.jeffery.notegenius.repository;

import com.jeffery.notegenius.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); // 原本就有，保留
    Optional<User> findByEmailAndUsername(String email, String username);
}
