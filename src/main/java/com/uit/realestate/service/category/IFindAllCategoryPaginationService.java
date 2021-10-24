package com.uit.realestate.service.category;

import com.uit.realestate.dto.category.CategoryDto;
import com.uit.realestate.dto.response.PaginationRequest;
import com.uit.realestate.dto.response.PaginationResponse;
import com.uit.realestate.service.IService;
import lombok.Data;

public interface IFindAllCategoryPaginationService extends IService<IFindAllCategoryPaginationService.Input, PaginationResponse<CategoryDto>> {
    @Data
    class Input extends PaginationRequest {
        private String search;
        public Input(String search, Integer page, Integer size){
            super(page, size);
            this.search = search;
        }
    }
}
