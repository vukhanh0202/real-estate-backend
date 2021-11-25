package com.uit.realestate.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uit.realestate.dto.SuitabilityDto;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CalculatorPercentSuitability {

    private static final String suitability = "SUITABILITY";
    private static final String total = "TOTAL";

    public static double calculatorPercent(SuitabilityDto suitabilityDto, String district, String province, String price, String category,
                                         Long floorQuantity, Long bedroomQuantity, Long bathroomQuantity){
        Map<String, Long> result = new HashMap<>();
        result.put(suitability, 0L);
        result.put(total, 0L);
        result = addValueByKey(result, calculate(suitabilityDto.getDistrictId(), district));
        result = addValueByKey(result, calculate(suitabilityDto.getProvinceId(), province));
        result = addValueByKey(result, calculate(suitabilityDto.getCategoryId(), category));
        Long score = result.get(suitability);
        Long scoreTotal = result.get(total);
        if (scoreTotal == 0L){
            return 0;
        }else{
            return Math.ceil((double)score / scoreTotal * 100);
        }
    }

    private static Map<String, Long> addValueByKey(Map<String, Long> mapPresent, Map<String, Long> mapAdd){
        Map<String, Long> result = new HashMap<>();
        result.put(suitability, mapPresent.get(suitability) + mapAdd.get(suitability));
        result.put(total, mapPresent.get(total) + mapAdd.get(total));
        return result;
    }

    private static Map<String, Long> calculate(Long id, String json){
        Long suitableTemp = 0L, totalTemp = 0L;
        if (json != null) {
            Map<String, Object> result = castJsonToMap(json);
            Set<Long> districtList = result.keySet().stream().map(Long::valueOf).collect(Collectors.toSet());
            for (Long item: districtList) {
                if (item.equals(id)){
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

    private static Map<String, Object> castJsonToMap(String json){
        try {
            return new ObjectMapper().readValue(json, HashMap.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

}
