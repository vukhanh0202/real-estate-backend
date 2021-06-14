package com.uit.realestate.constant.enums.apartment;

import lombok.Getter;

public enum ETypeApartment {
    BUY("Bán"),
    RENT("Cho Thuê");

    @Getter
    private final String value;

    ETypeApartment(String value){
        this.value = value;
    }
}
