package com.uit.realestate.utils;

import com.uit.realestate.domain.user.UserTarget;
import com.uit.realestate.dto.SuitabilityDto;
import com.uit.realestate.dto.statistic.UnitDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class CalculatorUnit {

    public static UnitDto calculatorUnit(Long gap) {
        Long unit;
        String unitCoinStr;
        String unitAreaStr;
        if (gap < 0){
            unit = 1L;
            unitCoinStr = " VNĐ";
            unitAreaStr = " m2";
        } else if (gap < 1000000L) {
            unit = 1L;
            unitCoinStr = " VNĐ";
            unitAreaStr = " m2";
        } else if (gap < 1000000000L) {
            unit = 1000000L;
            unitCoinStr = " Triệu VNĐ";
            unitAreaStr = " Triệu m2";
        } else if (gap < 1000000000000L) {
            unit = 1000000000L;
            unitCoinStr = " Tỷ VNĐ";
            unitAreaStr = " Tỷ m2";
        } else {
            unit = 1000000000000L;
            unitCoinStr = " Nghìn Tỷ";
            unitAreaStr = " Nghìn Tỷ m2";
        }
        return new UnitDto(unit, unitCoinStr, unitAreaStr);
    }
}
