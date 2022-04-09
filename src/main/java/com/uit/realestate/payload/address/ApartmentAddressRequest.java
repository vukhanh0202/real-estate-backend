package com.uit.realestate.payload.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApartmentAddressRequest {

    @JsonProperty("country_code")
    private String countryCode;

    @JsonProperty("province_id")
    private Long provinceId;

    @JsonProperty("district_id")
    private Long districtId;

    private String address;
}
