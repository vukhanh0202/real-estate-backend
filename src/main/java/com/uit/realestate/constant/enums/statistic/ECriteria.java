package com.uit.realestate.constant.enums.statistic;

import lombok.Getter;
import lombok.Setter;

public enum ECriteria {
    CITY,
    AREA,
    PRICE;

    @Getter
    @Setter
    private Double from;

    @Getter
    @Setter
    private Double to;

    ECriteria() {
    }

    ECriteria(Double from, Double to) {
        this.from = from;
        this.to = to;
    }
}
