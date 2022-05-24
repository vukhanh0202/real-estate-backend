package com.uit.realestate.payload.apartment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.payload.CatchInfoRequest;
import com.uit.realestate.payload.CatchInfoRequestExt;
import lombok.Data;

import java.util.List;

@Data
public class SearchApartmentRequest extends CatchInfoRequestExt {

    private Long districtId;
    private Long provinceId;
    private Double priceFrom;
    private Double priceTo;
    private Double areaFrom;
    private Double areaTo;
    private Long categoryId;
    private ETypeApartment typeApartment;
    @JsonIgnore
    private EApartmentStatus apartmentStatus;
    private String search;
    private Long houseDirection;
    private Long bedroomQuantity;
    private Long bathroomQuantity;
    private Long floorQuantity;

    public SearchApartmentRequest(Integer page, Integer size, Long districtId, Long provinceId, Double priceFrom, Double priceTo,
                 Double areaFrom, Double areaTo, Long categoryId, ETypeApartment typeApartment, EApartmentStatus status,
                 Long userId, String search, Long houseDirection, Long bedroomQuantity, Long bathroomQuantity, Long floorQuantity) {
        super(userId, page, size);
        this.districtId = districtId;
        this.provinceId = provinceId;
        this.priceFrom = priceFrom;
        if (priceTo != null){
            this.priceTo = priceTo == -1 ? null : priceTo;
        }
        this.areaFrom = areaFrom;
        if (areaTo != null){
            this.areaTo = areaTo == -1 ? null : areaTo;
        }
        this.categoryId = categoryId;
        this.typeApartment = typeApartment;
        this.apartmentStatus = status;
        this.search = search;
        this.houseDirection = houseDirection;
        this.bedroomQuantity = bedroomQuantity;
        this.bathroomQuantity = bathroomQuantity;
        this.floorQuantity = floorQuantity;
    }
}
