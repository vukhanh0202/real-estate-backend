package com.uit.realestate.utils;

import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.dto.statistic.TotalStatisticDto;
import com.uit.realestate.exception.InvalidException;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public final class StringUtils {

    private static final double BILLION = 1000000000;
    private static final double MILLION = 1000000;

    public static Double castNumberFromString(String str) {
        try {
            Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?(\\,\\d+)?");
            String matcher = pattern.matcher(str).results()
                    .collect(Collectors.toList())
                    .get(0)
                    .group(0);
            return Double.parseDouble(matcher.replace(",", "."));
        } catch (Exception e) {
            return 0D;
        }
    }

    public static Double castNumberFromStringPrice(String str) {
        Double price = castNumberFromString(str);
        if (str.toLowerCase().contains("tỷ")) {
            return price * BILLION;
        } else if (str.toLowerCase().contains("triệu")) {
            return price * 1000000;
        } else {
            throw new InvalidException("Not cast price");
        }
    }

    public static String castPriceFromNumber(Double price) {
        DecimalFormat df = new DecimalFormat("###,###,###");
        if (price > BILLION) {
            return df.format(Math.round(price / BILLION)) + " tỷ";
        } else if (price > MILLION) {
            return df.format(Math.round(price / MILLION)) + " triệu";
        } else {
            return df.format(price);
        }
    }

    public static String concat(String str, int limit) {
        if (str.length() > limit){
            return str.substring(0, limit);
        }
        return str;
    }
}
