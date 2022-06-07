package com.uit.realestate.constant.enums.apartment;

import lombok.Getter;

public enum EApartmentStatus {
    OPEN("Mở"),
    PENDING("Chờ"),
    CLOSE("Đóng");

    @Getter
    private final String value;

    EApartmentStatus(String value){
        this.value = value;
    }
}
