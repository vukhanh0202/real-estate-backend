package com.uit.realestate.service.apartment;

import com.uit.realestate.service.IService;
import lombok.Data;

public interface IValidateApartmentService<Input, Output> extends IService<Input, Output> {

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
