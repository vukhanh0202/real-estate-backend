package com.uit.realestate.payload.apartment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.dto.response.FileCaption;
import com.uit.realestate.payload.address.ApartmentAddressRequest;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UpdateApartmentRequest {

    @JsonIgnore
    private Long id;

    @JsonIgnore
    private Long authorId;

    private String title;

    private String overview;

    private Double area;

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

    private List<FileCaption> photos;

    @JsonIgnore
    private Boolean isAdmin = false;
}
