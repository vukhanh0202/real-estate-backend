package com.uit.realestate.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uit.realestate.constant.SuitabilityConstant;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.domain.user.UserTarget;
import com.uit.realestate.dto.SuitabilityDto;

import java.util.*;
import java.util.stream.Collectors;

public class CalculatorPercentSuitability {

    private static final String suitability = "SUITABILITY";
    private static final String total = "TOTAL";

    public static double calculatorPercent(SuitabilityDto suitabilityDto, List<UserTarget> userTargets) {
        Map<String, Long> result = new HashMap<>();
        result.put(suitability, 0L);
        result.put(total, 0L);
        result = addValueByKey(result, calculate(suitabilityDto.getDistrictId(),
                userTargets.stream().map(UserTarget::getDistrict).filter(Objects::nonNull).collect(Collectors.toList())));
        result = addValueByKey(result, calculate(suitabilityDto.getProvinceId(),
                userTargets.stream().map(UserTarget::getProvince).filter(Objects::nonNull).collect(Collectors.toList())));
        result = addValueByKey(result, calculate(suitabilityDto.getCategoryId(),
                userTargets.stream().map(UserTarget::getCategory).filter(Objects::nonNull).collect(Collectors.toList())));
        result = addValueByKey(result, calculateAccuracy(suitabilityDto.getFloorQuantity(),
                userTargets.stream().map(UserTarget::getFloorQuantity).filter(Objects::nonNull).collect(Collectors.toList())));
        result = addValueByKey(result, calculateAccuracy(suitabilityDto.getBedroomQuantity(),
                userTargets.stream().map(UserTarget::getBedroomQuantity).filter(Objects::nonNull).collect(Collectors.toList())));
        result = addValueByKey(result, calculateAccuracy(suitabilityDto.getBathroomQuantity(),
                userTargets.stream().map(UserTarget::getBathroomQuantity).filter(Objects::nonNull).collect(Collectors.toList())));
        result = addValueByKey(result, calculateArea(suitabilityDto.getArea(),
                userTargets.stream().map(UserTarget::getArea).filter(Objects::nonNull).collect(Collectors.toList())));
        result = addValueByKey(result, calculatePrice(suitabilityDto.getTotalPrice(),
                userTargets.stream().map(UserTarget::getPrice).filter(Objects::nonNull).collect(Collectors.toList())));
        Long score = result.get(suitability);
        Long scoreTotal = result.get(total);
        if (scoreTotal == 0L) {
            return 0;
        } else {
            return Math.ceil((double) score / scoreTotal * 100);
        }
    }

    private static Map<String, Long> addValueByKey(Map<String, Long> mapPresent, Map<String, Long> mapAdd) {
        Map<String, Long> result = new HashMap<>();
        result.put(suitability, mapPresent.get(suitability) + mapAdd.get(suitability));
        result.put(total, mapPresent.get(total) + mapAdd.get(total));
        return result;
    }

    private static Map<String, Long> calculatePrice(Double price, List<Double> prices) {
        long score = 0L, scoreTotal = 0L;
        long rangPrice = (long) (price * 0.1);
        if (rangPrice < SuitabilityConstant.DEFAULT_ACCURACY_PRICE) {
            rangPrice = SuitabilityConstant.DEFAULT_ACCURACY_PRICE;
        }
        if (!prices.isEmpty()) {
            for (Double item : prices) {
                if (Math.abs(price - item) >= rangPrice) {
                    score = 0L;
                } else {
                    score = Math.round(rangPrice - Math.abs(price - item));
                }
                scoreTotal += rangPrice;
            }
        }
        Map<String, Long> result = new HashMap<>();
        result.put(suitability, score * SuitabilityConstant.DEFAULT_ACCURACY_PRICE / rangPrice);
        result.put(total, scoreTotal * SuitabilityConstant.DEFAULT_ACCURACY_PRICE / rangPrice);
        return result;
    }

    private static Map<String, Long> calculateArea(Double area, List<Double> areas) {
        long score = 0L, scoreTotal = 0L;
        long rangArea = (long) (area * 0.1);
        if (rangArea < SuitabilityConstant.DEFAULT_ACCURACY_AREA) {
            rangArea = SuitabilityConstant.DEFAULT_ACCURACY_AREA;
        }
        if (!areas.isEmpty()) {
            for (Double item : areas) {
                if (Math.abs(area - item) >= rangArea) {
                    score = 0L;
                } else {
                    score = Math.round(rangArea - Math.abs(area - item));
                }
                scoreTotal += rangArea;
            }
        }
        Map<String, Long> result = new HashMap<>();
        result.put(suitability, score * SuitabilityConstant.DEFAULT_ACCURACY_AREA / rangArea);
        result.put(total, scoreTotal * SuitabilityConstant.DEFAULT_ACCURACY_AREA / rangArea);
        return result;
    }

    private static Map<String, Long> calculate(Long id, List<Long> arr) {
        long suitableTemp = 0L, totalTemp = 0L;
        if (!arr.isEmpty()) {
             for (Long item : arr) {
                if (item.equals(id)) {
                    suitableTemp += SuitabilityConstant.DEFAULT_ACCURACY;
                }
                totalTemp += SuitabilityConstant.DEFAULT_ACCURACY;
            }
        }
        Map<String, Long> result = new HashMap<>();
        result.put(suitability, suitableTemp);
        result.put(total, totalTemp);
        return result;
    }

    private static Map<String, Long> calculateAccuracy(Long expected, List<Long> presents) {
        long score = 0L, scoreTotal = 0L;
        if (expected != null && !presents.isEmpty()) {
            for (Long item : presents) {
                if (Math.abs(expected - item) >= SuitabilityConstant.DEFAULT_ACCURACY) {
                    score += 0L;
                } else {
                    score += SuitabilityConstant.DEFAULT_ACCURACY - Math.abs(expected - item);
                }
                scoreTotal += SuitabilityConstant.DEFAULT_ACCURACY;
            }
        }
        Map<String, Long> result = new HashMap<>();
        result.put(suitability, score);
        result.put(total, scoreTotal);
        return result;
    }

}
