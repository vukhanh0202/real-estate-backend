package com.uit.realestate.service.apartment;

import com.uit.realestate.dto.apartment.ApartmentBasicDto;
import com.uit.realestate.dto.response.PaginationRequest;
import com.uit.realestate.dto.response.PaginationResponse;
import com.uit.realestate.service.IService;
import lombok.Data;

public interface IFindSimilarApartmentService extends IService<IFindSimilarApartmentService.Input, PaginationResponse<ApartmentBasicDto>> {

    @Data
    class Input extends PaginationRequest {

        private Long userId;
        private String ip;

        public Input(Integer page, Integer size, Long userId, String ip) {
            super(page, size);
            this.userId = userId;
            this.ip = ip;
        }
    }
}
