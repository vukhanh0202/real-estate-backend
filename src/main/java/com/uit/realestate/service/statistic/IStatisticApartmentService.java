package com.uit.realestate.service.statistic;

import com.uit.realestate.constant.enums.statistic.ECriteria;
import com.uit.realestate.constant.enums.statistic.EStatistic;
import com.uit.realestate.dto.statistic.StatisticDto;

public interface IStatisticApartmentService{
    StatisticDto executeStatistic(EStatistic statistic, ECriteria criteria, Long userId);
}
