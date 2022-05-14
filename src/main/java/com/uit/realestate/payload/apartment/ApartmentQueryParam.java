package com.uit.realestate.payload.apartment;

import com.uit.realestate.constant.enums.EEntitiesDialog;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.payload.CatchInfoRequestExt;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class ApartmentQueryParam extends CatchInfoRequestExt {

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

    private String type;

    public static ApartmentQueryParam of(Map<String, Object> rq) {
        ApartmentQueryParam param = new ApartmentQueryParam();
        for (String key : rq.keySet()) {
            EEntitiesDialog.setValueIntoApartmentQueryParam(param, key, rq.get(key).toString());
        }
        return param;
    }

    public String generateKey() {
        return this.getUserId() + "_" + this.getIp() + "_" + LocalDate.now();
    }

}
