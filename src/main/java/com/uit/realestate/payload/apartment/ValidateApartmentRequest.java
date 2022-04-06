package com.uit.realestate.payload.apartment;

import lombok.Data;

@Data
public class ValidateApartmentRequest {

    private Long id;
    private Boolean decision;

    public ValidateApartmentRequest(Long id, Boolean decision) {
        this.id = id;
        this.decision = decision;
    }
}
