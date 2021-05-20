package com.uit.realestate.payload.apartment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ApartmentDetailRequest {

    private String description;

    @JsonProperty("front_building")
    private String frontBuilding;

    @JsonProperty("entrance_building")
    private String entranceBuilding;

    @JsonProperty("house_direction")
    private String houseDirection;

    @JsonProperty("balcony_direction")
    private String balconyDirection;

    @JsonProperty("floor_quantity")
    private Integer floorQuantity;

    @JsonProperty("bedroom_quantity")
    private Integer bedroomQuantity;

    @JsonProperty("bathroom_quantity")
    private Integer bathroomQuantity;

    @JsonProperty("toilet_quantity")
    private Integer toiletQuantity;

    private String furniture;
}
