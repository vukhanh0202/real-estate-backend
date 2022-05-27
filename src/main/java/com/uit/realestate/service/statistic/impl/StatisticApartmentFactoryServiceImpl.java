package com.uit.realestate.service.statistic.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.constant.enums.statistic.ECriteria;
import com.uit.realestate.constant.enums.statistic.EStatistic;
import com.uit.realestate.dto.statistic.StatisticDto;
import com.uit.realestate.exception.InvalidException;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.statistic.IStatisticApartmentFactoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticApartmentFactoryServiceImpl extends AbstractBaseService<IStatisticApartmentFactoryService.Input, StatisticDto>
        implements IStatisticApartmentFactoryService {

    private final StatisticCityByPriceServiceImpl statisticCityByPriceService;
    private final StatisticCityByAreaServiceImpl statisticCityByAreaService;
    private final StatisticAreaByCityServiceImpl statisticAreaByCityService;
    private final StatisticAreaByPriceServiceImpl statisticAreaByPriceService;
    private final StatisticPriceByCityServiceImpl statisticPriceByCityService;
    private final StatisticPriceByAreaServiceImpl statisticPriceByAreaService;

    @Override
    public StatisticDto doing(Input input) {
        if (input.getStatistic() == EStatistic.CITY) {
            if (input.getCriteria() == ECriteria.PRICE) {
                return statisticCityByPriceService.executeStatistic(input.getStatistic(), input.getCriteria(), input.getUserId(), input.getIp(), input.getTypeApartment());
            } else if (input.getCriteria() == ECriteria.AREA) {
                return statisticCityByAreaService.executeStatistic(input.getStatistic(), input.getCriteria(), input.getUserId(), input.getIp(), input.getTypeApartment());
            }
        } else if (input.getStatistic() == EStatistic.AREA) {
            if (input.getCriteria() == ECriteria.CITY) {
                return statisticAreaByCityService.executeStatistic(input.getStatistic(), input.getCriteria(), input.getUserId(), input.getIp(), input.getTypeApartment());
            } else if (input.getCriteria() == ECriteria.PRICE) {
                return statisticAreaByPriceService.executeStatistic(input.getStatistic(), input.getCriteria(), input.getUserId(), input.getIp(), input.getTypeApartment());
            }
        } else if (input.getStatistic() == EStatistic.PRICE) {
            if (input.getCriteria() == ECriteria.CITY) {
                return statisticPriceByCityService.executeStatistic(input.getStatistic(), input.getCriteria(), input.getUserId(), input.getIp(), input.getTypeApartment());
            } else if (input.getCriteria() == ECriteria.AREA) {
                return statisticPriceByAreaService.executeStatistic(input.getStatistic(), input.getCriteria(), input.getUserId(), input.getIp(), input.getTypeApartment());
            }
        }
        throw new InvalidException(messageHelper.getMessage(MessageCode.System.INVALID));
    }
}
