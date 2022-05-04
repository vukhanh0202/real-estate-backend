package com.uit.realestate.payload.apartment;

import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.payload.CatchInfoRequestExt;
import lombok.Data;

@Data
public class HighlightApartmentRequest extends CatchInfoRequestExt {

    private ETypeApartment typeApartment;

    private Long provinceId;

    public HighlightApartmentRequest(Long userId, ETypeApartment typeApartment, Long provinceId) {
        super(userId);
        this.typeApartment = typeApartment;
        this.provinceId = provinceId;
    }
}
