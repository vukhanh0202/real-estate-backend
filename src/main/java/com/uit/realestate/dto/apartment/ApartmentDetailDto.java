package com.uit.realestate.dto.apartment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uit.realestate.dto.BaseDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApartmentDetailDto{

    private String description;

    @JsonProperty("front_building")
    private String frontBuilding;

    @JsonProperty("entrance_building")
    private String entranceBuilding;

    @JsonProperty("house_building")
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
