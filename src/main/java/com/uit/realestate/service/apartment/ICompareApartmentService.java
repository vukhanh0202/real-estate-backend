package com.uit.realestate.service.apartment;

import com.uit.realestate.dto.apartment.ApartmentCompareDto;
import com.uit.realestate.service.IService;
import lombok.Data;

import java.util.List;

public interface ICompareApartmentService extends IService<ICompareApartmentService.Input, List<ApartmentCompareDto>> {

    @Data
    class Input {
        List<Long> ids;
        Long userId;

        public Input(List<Long> ids, Long userId) {
            this.ids = ids;
            this.userId = userId;
        }
    }
}
