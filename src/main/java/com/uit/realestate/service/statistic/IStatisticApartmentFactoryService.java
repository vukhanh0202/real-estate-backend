package com.uit.realestate.service.statistic;

import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.constant.enums.statistic.ECriteria;
import com.uit.realestate.constant.enums.statistic.EStatistic;
import com.uit.realestate.dto.statistic.StatisticDto;
import com.uit.realestate.service.IService;
import lombok.Data;

public interface IStatisticApartmentFactoryService extends IService<IStatisticApartmentFactoryService.Input, StatisticDto> {

    @Data
    class Input{
        private EStatistic statistic;
        private ECriteria criteria;
        private Long userId;
        private ETypeApartment typeApartment;

        public Input(EStatistic statistic, ECriteria criteria, Long userId, ETypeApartment typeApartment) {
            this.statistic = statistic;
            this.criteria = criteria;
            this.userId = userId;
            this.typeApartment = typeApartment;
        }
    }
}
