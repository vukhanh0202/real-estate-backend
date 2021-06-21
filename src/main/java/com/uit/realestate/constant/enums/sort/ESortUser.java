package com.uit.realestate.constant.enums.sort;

import lombok.Getter;

public enum ESortUser {
    ID("id"),
    FULL_NAME("fullName"),
    EMAIL("email");

    @Getter
    private String value;

    ESortUser(String value){
        this.value = value;
    }
}
