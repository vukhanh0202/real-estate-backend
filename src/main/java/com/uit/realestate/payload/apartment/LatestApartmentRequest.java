package com.uit.realestate.payload.apartment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.payload.CatchInfoRequest;
import com.uit.realestate.payload.CatchInfoRequestExt;
import lombok.Data;

@Data
public class LatestApartmentRequest extends CatchInfoRequest {

    @JsonProperty("type_apartment")
    private ETypeApartment typeApartment;

    public LatestApartmentRequest(Long userId, ETypeApartment typeApartment) {
        super(userId);
        this.typeApartment = typeApartment;
    }
}
