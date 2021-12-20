package com.uit.realestate.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.domain.user.User;
import com.uit.realestate.dto.location.DistrictDto;
import com.uit.realestate.dto.location.ProvinceDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserTargetDto {


    private Long id;
    private String district;
    private String province;
    private String price;
    @JsonProperty("floor_quantity")
    private Long floorQuantity;
    @JsonProperty("bedroom_quantity")
    private Long bedroomQuantity;
    @JsonProperty("bathroom_quantity")
    private Long bathroomQuantity;
    @JsonProperty("type_apartment")
    private ETypeApartment typeApartment;
    private String area;
    private Long category;
    @JsonProperty("district_name")
    private String districtName;
    @JsonProperty("province_name")
    private String provinceName;
    @JsonProperty("category_name")
    private String categoryName;
}
