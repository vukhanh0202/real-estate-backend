package com.uit.realestate.service.user;

import com.uit.realestate.dto.response.PaginationRequest;
import com.uit.realestate.service.IService;
import lombok.Data;

public interface IFindAllUserService<Input, Output> extends IService<Input, Output> {

    @Data
    class Input extends PaginationRequest {
        private String search;
        public Input(Integer page, Integer size, String search) {
            super(page, size);
            this.search = search;
        }
    }
}
