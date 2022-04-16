package com.uit.realestate.dto.apartment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.dto.BaseDto;
import com.uit.realestate.dto.response.FileCaption;
import com.uit.realestate.dto.user.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApartmentSearchDto extends BaseDto {

    private Long id;

    private String title;

    private String address;

    private ApartmentAddressDto addressDetail;

    private Double area;

    @JsonProperty("total_price")
    private String totalPrice;

    @JsonProperty("percent_suitable")
    private Double percentSuitable;
}
