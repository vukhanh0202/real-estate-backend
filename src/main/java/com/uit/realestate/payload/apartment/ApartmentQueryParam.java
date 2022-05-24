package com.uit.realestate.payload.apartment;

import com.uit.realestate.constant.enums.EEntitiesDialog;
import com.uit.realestate.payload.CatchInfoRequestExt;
import com.uit.realestate.utils.JsonUtils;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
public class ApartmentQueryParam extends CatchInfoRequestExt {

    private Double areaRange;
    private Double areaLow;
    private Double areaHigh;

    public List<Integer> bathrooms = new ArrayList<>();

    private List<Integer> bedrooms = new ArrayList<>();

    private List<Long> categories = new ArrayList<>();

    private List<String> directions = new ArrayList<>();

    private List<Long> districts = new ArrayList<>();

    private List<Long> provinces = new ArrayList<>();

    private List<Integer> floors = new ArrayList<>();

    private Double priceLow;
    private Double priceHigh;
    private Double priceRange;

    private String type;

    public static ApartmentQueryParam of(Map<String, Object> rq) {
        ApartmentQueryParam param = new ApartmentQueryParam();
        for (String key : rq.keySet()) {
            EEntitiesDialog.setValueIntoApartmentQueryParam(param, key, rq.get(key).toString());
        }
        return param;
    }
    public boolean isEmpty() {
        return Objects.equals(JsonUtils.marshal(this), "{}");
    }


    public String generateKey() {
        return this.getUserId() + "_" + this.getIp() + "_" + LocalDate.now() + "_" + generateKeySearch();
    }

    public String generateSearch(){
        StringBuilder str = new StringBuilder();
        if(Objects.nonNull(areaLow)){
            str.append("&" + "area_from=").append(Math.round(areaLow));
        }

        if(Objects.nonNull(areaHigh)){
            str.append("&" + "area_to=").append(Math.round(areaLow));
        }

        if(Objects.nonNull(priceLow)){
            str.append("&" + "price_from=").append(Math.round(priceLow));
        }

        if(Objects.nonNull(priceHigh)){
            str.append("&" + "price_to=").append(Math.round(priceHigh));
        }

        if (!bathrooms.isEmpty()){
            str.append("&" + "bathroom_quantity=").append(bathrooms.get(0));
        }

        if (!bedrooms.isEmpty()){
            str.append("&" + "bedroom_quantity=").append(bedrooms.get(0));
        }

        if (!categories.isEmpty()){
            str.append("&" + "category_id=").append(categories.get(0));
        }

        if (!provinces.isEmpty()){
            str.append("&" + "province_id=").append(provinces.get(0));
        }

        if (!floors.isEmpty()){
            str.append("&" + "floor_quantity=").append(floors.get(0));
        }

        if (!districts.isEmpty()){
            str.append("&" + "district_id=").append(districts.get(0));
        }

        return str.toString();
    }

    private String generateKeySearch(){
        StringBuilder str = new StringBuilder();
        if(Objects.nonNull(areaLow)){
            str.append("-" + "area_from=").append(Math.round(areaLow));
        }

        if(Objects.nonNull(areaHigh)){
            str.append("-" + "area_to=").append(Math.round(areaLow));
        }

        if(Objects.nonNull(priceLow)){
            str.append("-" + "price_from=").append(Math.round(priceLow));
        }

        if(Objects.nonNull(priceHigh)){
            str.append("-" + "price_to=").append(Math.round(priceHigh));
        }

        if (!bathrooms.isEmpty()){
            str.append("-" + "bathroom_quantity=").append(bathrooms.get(0));
        }

        if (!bedrooms.isEmpty()){
            str.append("-" + "bedroom_quantity=").append(bedrooms.get(0));
        }

        if (!categories.isEmpty()){
            str.append("-" + "category_id=").append(categories.get(0));
        }

        if (!provinces.isEmpty()){
            str.append("-" + "province_id=").append(provinces.get(0));
        }

        if (!floors.isEmpty()){
            str.append("-" + "floor_quantity=").append(floors.get(0));
        }

        if (!districts.isEmpty()){
            str.append("-" + "district_id=").append(districts.get(0));
        }

        return str.toString();
    }
}
