package com.uit.realestate.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuitabilityDto extends BaseDto {

    @JsonProperty("province_id")
    private Long provinceId;

    @JsonProperty("district_id")
    private Long districtId;

    @JsonProperty("category_id")
    private Long categoryId;

    @JsonProperty("total_price")
    private Double totalPrice;

    private Double area;

    @JsonProperty("bedroom_quantity")
    private Long bedroomQuantity;

    @JsonProperty("bathroom_quantity")
    private Long bathroomQuantity;

    @JsonProperty("floor_quantity")
    private Long floorQuantity;

}
