package com.uit.realestate.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uit.realestate.dto.location.DistrictDto;
import com.uit.realestate.dto.location.ProvinceDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserTargetDto {
    private DistrictDto district;
    private ProvinceDto province;
    private Double price;
    @JsonProperty("house_direction")
    private String houseDirection;
    @JsonProperty("floor_quantity")
    private Long floorQuantity;
    @JsonProperty("bedroom_quantity")
    private Long bedroomQuantity;
    @JsonProperty("bathroom_quantity")
    private Long bathroomQuantity;
}
