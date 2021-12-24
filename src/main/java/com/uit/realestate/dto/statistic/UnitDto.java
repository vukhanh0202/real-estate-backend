package com.uit.realestate.dto.statistic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.uit.realestate.dto.apartment.ApartmentBasicDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnitDto {

    private Long unit;

    private String unitCoinStr;

    private String unitAreaStr;

    public UnitDto(Long unit, String unitCoinStr, String unitAreaStr) {
        this.unit = unit;
        this.unitCoinStr = unitCoinStr;
        this.unitAreaStr = unitAreaStr;
    }
}
