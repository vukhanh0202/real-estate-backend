package com.uit.realestate.constant.enums.apartment;

import com.uit.realestate.exception.InvalidException;
import lombok.Getter;

public enum ETypeApartment {
    BUY("Bán","Cần bán"),
    RENT("Thuê","Cho thuê");

    @Getter
    private final String value;

    @Getter
    private final String displayName;

    ETypeApartment(String value, String displayName){
        this.value = value;
        this.displayName = displayName;
    }

    public static ETypeApartment of(String str){
        for(ETypeApartment type : values()){
            if(str.toUpperCase().contains(type.getValue())){
                return type;
            }
        }
        throw new InvalidException("Error");
    }
}
