package com.uit.realestate.payload.apartment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.payload.address.ApartmentAddressRequest;
import lombok.Data;

import java.util.Date;

@Data
public class UpdateApartmentRequest {

    @JsonIgnore
    private Long id;

    @JsonIgnore
    private Long authorId;

    private String title;

    private String overview;

    private Double area;

    private Double price;

    @JsonProperty("total_price")
    private Double totalPrice;

    @JsonProperty("type_apartment")
    private ETypeApartment typeApartment;

    @JsonProperty("expired_date")
    private Date expiredDate;

    @JsonProperty("apartment_address")
    private ApartmentAddressRequest apartmentAddress;

    @JsonProperty("category_id")
    private Long categoryId;

    @JsonProperty("apartment_detail")
    private ApartmentDetailRequest apartmentDetail;
}
