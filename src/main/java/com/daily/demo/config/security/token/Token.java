package com.daily.demo.config.security.token;

import com.daily.demo.entity.daily.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class Token extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_email", length = 1024, nullable = false)
    private String userEmail;

    @Column(name = "refresh_token", length = 1024, nullable = false)
    private String refreshToken;

    public Token() {
    }

    public Token updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    // @Builder
    // public Token(String userEmail, String refreshToken){
    // this.userEmail = userEmail;
    // this.refreshToken = refreshToken;
    // }
}
