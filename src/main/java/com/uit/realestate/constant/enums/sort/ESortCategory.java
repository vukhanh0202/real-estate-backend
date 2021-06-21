package com.uit.realestate.constant.enums.sort;

import lombok.Getter;

public enum ESortCategory {
    ID("id"),
    NAME("name");

    @Getter
    private String value;

    ESortCategory(String value){
        this.value = value;
    }
}
