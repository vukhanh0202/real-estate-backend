package com.uit.realestate.constant.enums;

import lombok.Getter;

public enum EScraper {
    PROPZY("https://propzy.vn"),
    BDS("batdongsan.vn"),
    ALOND("https://alonhadat.com.vn");

    @Getter
    private final String value;

    EScraper(String value){
        this.value = value;
    }
}
