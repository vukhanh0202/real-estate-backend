package com.uit.realestate.payload.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddUserTargetRequest {

    @JsonIgnore
    private Long userId;
    @JsonProperty("district_id")
    private Long districtId;
    @JsonProperty("province_id")
    private Long provinceId;
    private Double price;
    @JsonProperty("floor_quantity")
    private Long floorQuantity;
    @JsonProperty("bedroom_quantity")
    private Long bedroomQuantity;
    @JsonProperty("bathroom_quantity")
    private Long bathroomQuantity;
    private Double area;
    private Long category;

}
