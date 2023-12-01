package com.daily.demo.entity.daily.enumData;

public enum Useyn {
    Y("공개"), N("비공개");

    private String description;

    private Useyn(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
