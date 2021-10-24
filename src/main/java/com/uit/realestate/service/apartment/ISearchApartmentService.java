package com.uit.realestate.service.apartment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.dto.apartment.ApartmentDto;
import com.uit.realestate.dto.response.PaginationRequest;
import com.uit.realestate.dto.response.PaginationResponse;
import com.uit.realestate.service.IService;
import lombok.Data;

public interface ISearchApartmentService extends IService<ISearchApartmentService.Input, PaginationResponse<ApartmentDto>> {

    /**
     * Param input.
     */
    @Data
    class Input extends PaginationRequest{
        private Long districtId;
        private Long provinceId;
        private Double priceFrom;
        private Double priceTo;
        private Double areaFrom;
        private Double areaTo;
        private Long categoryId;
        private ETypeApartment typeApartment;
        @JsonIgnore
        private EApartmentStatus apartmentStatus;
        private Long userId;
        private String search;

        public Input(Integer page, Integer size, Long districtId, Long provinceId, Double priceFrom, Double priceTo,
                     Double areaFrom, Double areaTo, Long categoryId, ETypeApartment typeApartment,Long userId,String search) {
            super(page, size);
            this.districtId = districtId;
            this.provinceId = provinceId;
            this.priceFrom = priceFrom;
            this.priceTo = priceTo;
            this.areaFrom = areaFrom;
            this.areaTo = areaTo;
            this.categoryId = categoryId;
            this.typeApartment = typeApartment;
            this.userId = userId;
            this.search = search;
        }

        public Input(Integer page, Integer size, Long districtId, Long provinceId, Double priceFrom, Double priceTo,
                     Double areaFrom, Double areaTo, Long categoryId, ETypeApartment typeApartment, EApartmentStatus status,
                     Long userId, String search) {
            super(page, size);
            this.districtId = districtId;
            this.provinceId = provinceId;
            this.priceFrom = priceFrom;
            this.priceTo = priceTo;
            this.areaFrom = areaFrom;
            this.areaTo = areaTo;
            this.categoryId = categoryId;
            this.typeApartment = typeApartment;
            this.apartmentStatus = status;
            this.userId = userId;
            this.search = search;
        }
    }
}
