package com.uit.realestate.service.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.payload.user.UpdateUserRequest;
import com.uit.realestate.service.IService;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public interface IAddUserTargetByTokenService extends IService<IAddUserTargetByTokenService.Input, Boolean> {

    @Data
    class Input{
        @JsonIgnore
        private Long userId;
        @JsonProperty("district_id")
        private Long districtId;
        @JsonProperty("province_id")
        private Long provinceId;
        private Double price;
        @JsonProperty("floor_quantity")
        private Long floorQuantity;
        @JsonProperty("bedroom_quantity")
        private Long bedroomQuantity;
        @JsonProperty("bathroom_quantity")
        private Long bathroomQuantity;
        private Double area;
        private Long category;
    }
}
