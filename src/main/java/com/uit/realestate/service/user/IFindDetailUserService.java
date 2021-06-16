package com.uit.realestate.service.user;

import com.uit.realestate.dto.response.PaginationRequest;
import com.uit.realestate.service.IService;
import lombok.Data;

public interface IFindDetailUserService<Input, Output> extends IService<Input, Output> {

    @Data
    class Input extends PaginationRequest {

        public Input(Integer page, Integer size, Long userId) {
            super(page, size);
        }
    }
}
