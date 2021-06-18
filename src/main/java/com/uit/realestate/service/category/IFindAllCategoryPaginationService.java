package com.uit.realestate.service.category;

import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.dto.response.PaginationRequest;
import com.uit.realestate.service.IService;
import lombok.Data;

public interface IFindAllCategoryPaginationService<Input, Output> extends IService<Input, Output> {
    @Data
    class Input extends PaginationRequest {
        public Input(Integer page, Integer size){
            super(page, size);
        }
    }
}
