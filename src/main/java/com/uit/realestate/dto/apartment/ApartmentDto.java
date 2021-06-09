package com.uit.realestate.dto.apartment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.dto.BaseDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApartmentDto extends BaseDto {

    private Long id;

    private String code;

    private String title;

    private String overview;

    private String address;

    @JsonProperty("total_price")
    private Double totalPrice;

    private Double area;

    private EApartmentStatus status;

    @JsonProperty("apartment_detail")
    private ApartmentDetailDto apartmentDetail;
}
