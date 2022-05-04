package com.uit.realestate.payload.apartment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.payload.CatchInfoRequestExt;
import lombok.Data;

@Data
public class RecommendApartmentRequest extends CatchInfoRequestExt {

    @JsonProperty("type_apartment")
    private ETypeApartment typeApartment;

    public RecommendApartmentRequest(Long userId, String ip, Integer page, Integer size, ETypeApartment typeApartment) {
        super(userId, ip, page, size);
        this.typeApartment = typeApartment;
    }
}
