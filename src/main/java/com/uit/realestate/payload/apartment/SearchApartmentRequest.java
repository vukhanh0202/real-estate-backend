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
    private String typeApartment;
    @JsonIgnore
    private String apartmentStatus;
    private String search;
    private String houseDirection;
    private Long bedroomQuantity;
    private Long bathroomQuantity;
    private Long floorQuantity;
    private Boolean highlight;

    public SearchApartmentRequest(Integer page, Integer size, Long districtId, Long provinceId, Double priceFrom, Double priceTo,
                                  Double areaFrom, Double areaTo, Long categoryId, String typeApartment, EApartmentStatus status,
                                  Long userId, String ip, String search, String houseDirection, Long bedroomQuantity, Long bathroomQuantity, Long floorQuantity) {
        super(userId, ip, page, size);
        this.districtId = districtId;
        this.provinceId = provinceId;
        this.priceFrom = priceFrom;
        if (priceTo != null) {
            this.priceTo = priceTo == -1 ? null : priceTo;
        }
        this.areaFrom = areaFrom;
        if (areaTo != null) {
            this.areaTo = areaTo == -1 ? null : areaTo;
        }
        this.categoryId = categoryId;
        this.typeApartment = typeApartment;
        this.apartmentStatus = status.name();
        if (search != null){
            this.search = "%" + search + "%";
        }
        if (houseDirection != null){
            this.houseDirection = "%" + houseDirection + "%";
        }
        this.bedroomQuantity = bedroomQuantity;
        this.bathroomQuantity = bathroomQuantity;
        this.floorQuantity = floorQuantity;
    }
    public SearchApartmentRequest() {
    }
}
