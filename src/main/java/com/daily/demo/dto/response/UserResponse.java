package com.daily.demo.dto.response;

import com.daily.demo.entity.auth.Provider;
import com.daily.demo.entity.daily.enumData.Useyn;
import com.daily.demo.entity.user.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
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

    private String createdBy;
    private LocalDateTime createdDate;
    private String modifiedBy;
    private LocalDateTime modifiedDate;
}
