package com.uit.realestate.payload.apartment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ApartmentDetailRequest {

    private String description;

    @JsonProperty("house_direction")
    private String houseDirection;

    @JsonProperty("floor_quantity")
    private Integer floorQuantity;

    @JsonProperty("bedroom_quantity")
    private Integer bedroomQuantity;

    @JsonProperty("bathroom_quantity")
    private Integer bathroomQuantity;

    @JsonProperty("toilet_quantity")
    private Integer toiletQuantity;

    @JsonProperty("more_info")
    private List<String> moreInfo;

    public ApartmentDetailRequest() {
    }

    public ApartmentDetailRequest(String description, String houseDirection, Integer floorQuantity, Integer bedroomQuantity, Integer bathroomQuantity, Integer toiletQuantity, List<String> moreInfo) {
        this.description = description;
        this.houseDirection = houseDirection;
        this.floorQuantity = floorQuantity;
        this.bedroomQuantity = bedroomQuantity;
        this.bathroomQuantity = bathroomQuantity;
        this.toiletQuantity = toiletQuantity;
        this.moreInfo = moreInfo;
    }
}
