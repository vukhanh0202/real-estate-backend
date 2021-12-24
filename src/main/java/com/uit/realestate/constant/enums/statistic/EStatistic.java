package com.uit.realestate.constant.enums.statistic;

import lombok.Getter;
import lombok.Setter;

public enum EStatistic {
    CITY,
    AREA,
    PRICE;

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Double from;

    @Getter
    @Setter
    private Double to;

    EStatistic() {
    }

    EStatistic(Long id, Double from, Double to) {
        this.id = id;
        this.from = from;
        this.to = to;
    }
}
