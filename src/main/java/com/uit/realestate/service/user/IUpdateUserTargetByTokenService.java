package com.uit.realestate.service.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.uit.realestate.service.IService;
import lombok.Data;

public interface IUpdateUserTargetByTokenService extends IService<IUpdateUserTargetByTokenService.Input, Boolean> {

    @Data
    class Input{
        private Long id;
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
