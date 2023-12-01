package com.daily.demo.entity.user;

import lombok.Getter;

@Getter
public enum UserEnum {

    Y("Using", "계정 사용중"), N("NOT Using", "계정 사용중지");

    private String status;
    private String description;

    private UserEnum(String status, String description) {
        this.status = status;
        this.description = description;
    }

}
