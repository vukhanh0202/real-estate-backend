package com.uit.realestate.constant.enums.sort;

import lombok.Getter;

public enum ESortApartment {
    ID("id"),
    AREA("area"),
    TOTAL_PRICE("totalPrice");

    @Getter
    private String value;

    ESortApartment(String value){
        this.value = value;
    }
}
