package com.uit.realestate.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uit.realestate.constant.SuitabilityConstant;
import com.uit.realestate.dto.SuitabilityDto;

import java.util.*;
import java.util.stream.Collectors;

public class CalculatorPercentSuitability {

    private static final String suitability = "SUITABILITY";
    private static final String total = "TOTAL";

    public static double calculatorPercent(SuitabilityDto suitabilityDto, String district, String province, String price,
                                           String category, Long floorQuantity, Long bedroomQuantity, Long bathroomQuantity,
                                           String area) {
        Map<String, Long> result = new HashMap<>();
        result.put(suitability, 0L);
        result.put(total, 0L);
        result = addValueByKey(result, calculate(suitabilityDto.getDistrictId(), district));
        result = addValueByKey(result, calculate(suitabilityDto.getProvinceId(), province));
        result = addValueByKey(result, calculate(suitabilityDto.getCategoryId(), category));
        result = addValueByKey(result, calculate(suitabilityDto.getFloorQuantity(), floorQuantity));
        result = addValueByKey(result, calculate(suitabilityDto.getBedroomQuantity(), bedroomQuantity));
        result = addValueByKey(result, calculate(suitabilityDto.getBathroomQuantity(), bathroomQuantity));
        result = addValueByKey(result, calculateArea(suitabilityDto.getArea(), area));
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

    private static Map<String, Long> calculatePrice(Long id, String json) {
        return null;
    }

    private static Map<String, Long> calculateArea(Double area, String areas) {
        Long score = 0L, scoreTotal = 0L;
        if (areas != null) {
            List<Double> areaList = Arrays.stream(areas.split(",")).map(Double::valueOf).collect(Collectors.toList());
            for (Double item : areaList) {
                if (Math.abs(area - item) >= SuitabilityConstant.DEFAULT_RANGE_AREA) {
                    score = 0L;
                } else {
                    score = Math.round(SuitabilityConstant.DEFAULT_RANGE_AREA - Math.abs(area - item));
                }
                scoreTotal += SuitabilityConstant.DEFAULT_RANGE_AREA;
            }
        }
        Map<String, Long> result = new HashMap<>();
        result.put(suitability, score);
        result.put(total, scoreTotal);
        return result;
    }

    private static Map<String, Long> calculate(Long id, String json) {
        Long suitableTemp = 0L, totalTemp = 0L;
        if (json != null) {
            Map<String, Object> result = castJsonToMap(json);
            Set<Long> districtList = result.keySet().stream().map(Long::valueOf).collect(Collectors.toSet());
            for (Long item : districtList) {
                if (item.equals(id)) {
                    suitableTemp += Long.parseLong(String.valueOf(result.get(item.toString())));
                }
                totalTemp += Long.parseLong(String.valueOf(result.get(item.toString())));
            }
        }
        Map<String, Long> result = new HashMap<>();
        result.put(suitability, suitableTemp);
        result.put(total, totalTemp);
        return result;
    }

    private static Map<String, Long> calculate(Long expected, Long present) {
        long score, scoreTotal;
        if (expected == null || present == null) {
            score = 0L;
            scoreTotal = 0L;
        } else {
            if (Math.abs(expected - present) >= SuitabilityConstant.DEFAULT_ACCURACY) {
                score = 0L;
            } else {
                score = SuitabilityConstant.DEFAULT_ACCURACY - Math.abs(expected - present);
            }
            scoreTotal = SuitabilityConstant.DEFAULT_ACCURACY;
        }
        Map<String, Long> result = new HashMap<>();
        result.put(suitability, score);
        result.put(total, scoreTotal);
        return result;
    }

    private static Map<String, Object> castJsonToMap(String json) {
        try {
            return new ObjectMapper().readValue(json, HashMap.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

}
