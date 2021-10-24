package com.uit.realestate.service.user;

import com.uit.realestate.dto.apartment.ApartmentDto;
import com.uit.realestate.dto.response.PaginationRequest;
import com.uit.realestate.dto.response.PaginationResponse;
import com.uit.realestate.service.IService;
import lombok.Data;

public interface IFindUserApartmentAuthorService extends IService<IFindUserApartmentAuthorService.Input, PaginationResponse<ApartmentDto>> {

    @Data
    class Input extends PaginationRequest {

        private Long userId;

        public Input(Integer page, Integer size, Long userId) {
            super(page, size);
            this.userId = userId;
        }
    }
}
