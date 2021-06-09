package com.uit.realestate.service.apartment;

import com.uit.realestate.constant.enums.ETypeApartment;
import com.uit.realestate.dto.response.PaginationRequest;
import com.uit.realestate.payload.apartment.ApartmentSearch;
import com.uit.realestate.service.IService;
import lombok.Data;

public interface ISearchApartmentService<Input, Output> extends IService<Input, Output> {

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

        public Input(Integer page, Integer size, Long districtId, Long provinceId, Double priceFrom, Double priceTo,
                     Double areaFrom, Double areaTo, Long categoryId, ETypeApartment typeApartment) {
            super(page, size);
            this.districtId = districtId;
            this.provinceId = provinceId;
            this.priceFrom = priceFrom;
            this.priceTo = priceTo;
            this.areaFrom = areaFrom;
            this.areaTo = areaTo;
            this.categoryId = categoryId;
            this.typeApartment = typeApartment;
        }
    }
}
