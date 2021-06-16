package com.uit.realestate.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uit.realestate.dto.BaseDto;
import com.uit.realestate.dto.apartment.ApartmentBasicDto;
import com.uit.realestate.dto.apartment.ApartmentDto;
import com.uit.realestate.dto.response.FileCaption;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDetailDto extends BaseDto {

    private Long id;

    private Integer totalPostApartment;

    private Integer totalFavouriteApartment;

    private Integer totalRecommendApartment;

    private List<ApartmentDto> postApartmentList;

    private List<ApartmentDto> favouriteApartmentList;
}
