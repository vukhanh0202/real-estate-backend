package com.uit.realestate.dto.apartment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.dto.BaseDto;
import com.uit.realestate.dto.response.FileCaption;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    private ApartmentAddressDto addressDetail;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("total_price")
    private Double totalPrice;

    private Double area;

    private EApartmentStatus status;

    private Boolean favourite = false;

    @JsonProperty("apartment_detail")
    private ApartmentDetailDto apartmentDetail;

    @JsonProperty("type_apartment")
    private String typeApartment;

    private List<FileCaption> photos;
}
