package com.uit.realestate.service.apartment;

import com.uit.realestate.dto.response.PaginationRequest;
import com.uit.realestate.payload.apartment.ApartmentSearch;
import com.uit.realestate.service.IService;
import lombok.Data;

public interface ISearchApartmentService<Input, Output> extends IService<Input, Output> {

    /**
     * Param input.
     */
    @Data
    class Input extends PaginationRequest {
        private ApartmentSearch apartmentSearch;

        public Input(ApartmentSearch apartmentSearch, Integer page, Integer size) {
            super(page, size);
            this.apartmentSearch = apartmentSearch;
        }
    }
}
