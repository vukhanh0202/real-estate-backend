package com.uit.realestate.dto.apartment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uit.realestate.dto.BaseDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApartmentDetailDto{

    private String description;

    @JsonProperty("house_building")
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

}
