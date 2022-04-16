package com.uit.realestate.payload.apartment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.dto.response.FileCaption;
import com.uit.realestate.payload.address.ApartmentAddressRequest;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AddApartmentRequest {

    private String title;

    private String overview;

    private Double area;

    @JsonIgnore
    private EApartmentStatus status;

    @JsonProperty("total_price")
    private Double totalPrice;

    @JsonProperty("price_rent")
    private Double priceRent;

    @JsonProperty("price_rent")
    private String unitRent;

    @JsonProperty("type_apartment")
    private ETypeApartment typeApartment;

    @JsonProperty("apartment_address")
    private ApartmentAddressRequest apartmentAddress;

    @JsonProperty("category_id")
    private Long categoryId;

    @JsonProperty("apartment_detail")
    private ApartmentDetailRequest apartmentDetail;

    private List<FileCaption> photos;

    @JsonIgnore
    private Long authorId;
}
