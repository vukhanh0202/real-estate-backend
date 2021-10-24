package com.uit.realestate.service.apartment;

import com.uit.realestate.service.IService;
import lombok.Data;

public interface IValidateApartmentService extends IService<IValidateApartmentService.Input, Boolean> {

    /**
     * Param input.
     */
    @Data
    class Input {

        private Long id;
        private Boolean decision;

        public Input(Long id, Boolean decision) {
            this.id = id;
            this.decision = decision;
        }
    }
}
