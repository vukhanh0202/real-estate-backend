package com.uit.realestate.constant.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum ENumber {
    EMPTY(0D, ""),
    BILLION(1000000000D, "tỷ"),
    MILLION(1000000D, "triệu"),
    THOUSAND(1000D, "nghìn", "ngàn");

    @Getter
    private final Double value;

    @Getter
    private final String[] texts;

    ENumber(Double value, String... texts){
        this.value = value;
        this.texts = texts;
    }

    public static ENumber of(String str){
        for(ENumber item : values()){
            if(Arrays.stream(item.texts).collect(Collectors.toList()).contains(str)){
                return item;
            }
        }
        return EMPTY;
    }
}
