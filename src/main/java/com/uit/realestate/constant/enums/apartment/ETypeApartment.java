package com.uit.realestate.constant.enums.apartment;

import com.uit.realestate.exception.InvalidException;
import lombok.Getter;

public enum ETypeApartment {
    BUY("Bán"),
    RENT("Thuê");

    @Getter
    private final String value;

    ETypeApartment(String value){
        this.value = value;
    }

    public static ETypeApartment of(String str){
        for(ETypeApartment type : values()){
            if(str.contains(type.getValue())){
                return type;
            }
        }
        throw new InvalidException("Error");
    }
}
