package com.uit.realestate.payload.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserAddressRequest {

    @JsonProperty("country_code")
    private String countryCode;

    @JsonProperty("province_id")
    private Long provinceId;

    @JsonProperty("district_id")
    private Long districtId;

    private String address;
}
