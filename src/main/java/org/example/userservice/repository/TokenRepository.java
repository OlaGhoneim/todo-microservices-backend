package org.example.userservice.repository;

import org.example.userservice.entity.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<JwtToken, Long> {
    JwtToken findByToken(String token);
}