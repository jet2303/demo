package com.daily.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daily.demo.config.security.token.Token;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByUserEmail(String userEmail);

    Optional<Token> findByRefreshToken(String refreshToken);
}
