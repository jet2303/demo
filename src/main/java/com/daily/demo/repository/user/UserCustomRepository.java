package com.daily.demo.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daily.demo.dto.response.UserResponse;
import com.daily.demo.entity.daily.enumData.Useyn;
import com.daily.demo.entity.user.Users;

public interface UserCustomRepository {

    Optional<UserResponse> findEmail(String email, Useyn useyn);
}
