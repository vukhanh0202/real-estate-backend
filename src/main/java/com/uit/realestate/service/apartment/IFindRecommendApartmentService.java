package com.uit.realestate.service.apartment;

import com.uit.realestate.dto.response.PaginationRequest;
import com.uit.realestate.service.IService;
import lombok.Data;

public interface IFindRecommendApartmentService<Input, Output> extends IService<Input, Output> {

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
