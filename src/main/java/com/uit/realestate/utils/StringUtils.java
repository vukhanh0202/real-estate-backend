package com.uit.realestate.utils;

import com.uit.realestate.exception.InvalidException;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public final class StringUtils {

    public static Double castNumberFromString(String str) {
        try{
            Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?(\\,\\d+)?");
            String matcher = pattern.matcher(str).results()
                    .collect(Collectors.toList())
                    .get(0)
                    .group(0);
            return Double.parseDouble(matcher.replace(",","."));
        }catch (Exception e){
            return 0D;
        }
    }

    public static Double castNumberFromStringPriceBillion(String str) {
        Double price = castNumberFromString(str);
        if (str.toLowerCase().contains("tỷ")){
            return price * 1000000000;
        }else {
            throw new InvalidException("Not cast price");
        }
    }
}
