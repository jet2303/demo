package com.daily.demo.entity.user;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ROLE_ADMIN", "관리자"), USER("ROLE_USER", "일반 유저");

    private String value;
    private String description;

    private Role(String value, String description) {
        this.value = value;
        this.description = description;
    }
}
