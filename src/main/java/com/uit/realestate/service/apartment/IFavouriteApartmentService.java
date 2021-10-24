package com.uit.realestate.service.apartment;

import com.uit.realestate.service.IService;
import lombok.Data;

public interface IFavouriteApartmentService extends IService<IFavouriteApartmentService.Input, Boolean> {

    /**
     * Param input.
     */
    @Data
    class Input {

        private Long userId;
        private Long apartmentId;
        private String ip;

        public Input(Long userId, Long apartmentId,String ip) {
            this.userId = userId;
            this.apartmentId = apartmentId;
            this.ip = ip;
        }
    }
}
