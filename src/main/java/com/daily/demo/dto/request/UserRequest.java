package com.daily.demo.dto.request;

import com.daily.demo.entity.auth.Provider;
import com.daily.demo.entity.daily.enumData.Useyn;
import com.daily.demo.entity.user.Role;
import com.daily.demo.entity.user.UserEnum;
import com.daily.demo.entity.user.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRequest {

    private Long id;

    private String name;

    private String imageUrl;

    private Boolean emailVerified;

    private String email;

    // @JsonIgnore
    private String password;

    // @NotNull
    private Provider provider;

    private Role role;

    private String providerId;

    private Useyn useyn;

}
