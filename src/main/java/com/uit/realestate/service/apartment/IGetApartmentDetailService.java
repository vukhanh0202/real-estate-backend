package com.uit.realestate.service.apartment;

import com.uit.realestate.service.IService;
import lombok.Data;

public interface IGetApartmentDetailService<Input, Output> extends IService<Input, Output> {

    @Data
    class Input{
        private Long id;
        private String ip;
        private Long userId;

        public Input(Long id, String ip,Long userId) {
            this.id = id;
            this.ip = ip;
            this.userId = userId;
        }
    }
}
