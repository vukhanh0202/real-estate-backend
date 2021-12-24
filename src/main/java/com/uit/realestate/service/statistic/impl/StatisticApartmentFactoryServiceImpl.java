package com.uit.realestate.service.statistic.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.constant.enums.statistic.ECriteria;
import com.uit.realestate.constant.enums.statistic.EStatistic;
import com.uit.realestate.dto.location.CountryDto;
import com.uit.realestate.dto.statistic.StatisticDto;
import com.uit.realestate.exception.InvalidException;
import com.uit.realestate.mapper.location.CountryMapper;
import com.uit.realestate.repository.location.CountryRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.location.IFindAllCountryService;
import com.uit.realestate.service.statistic.IStatisticApartmentFactoryService;
import com.uit.realestate.service.statistic.IStatisticApartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
                return statisticCityByPriceService.executeStatistic(input.getStatistic(), input.getCriteria(), input.getUserId());
            } else if (input.getCriteria() == ECriteria.AREA) {
                return statisticCityByAreaService.executeStatistic(input.getStatistic(), input.getCriteria(), input.getUserId());
            }
        } else if (input.getStatistic() == EStatistic.AREA) {
            if (input.getCriteria() == ECriteria.CITY) {
                return statisticAreaByCityService.executeStatistic(input.getStatistic(), input.getCriteria(), input.getUserId());
            } else if (input.getCriteria() == ECriteria.PRICE) {
                return statisticAreaByPriceService.executeStatistic(input.getStatistic(), input.getCriteria(), input.getUserId());
            }
        } else if (input.getStatistic() == EStatistic.PRICE) {
            if (input.getCriteria() == ECriteria.CITY) {
                return statisticPriceByCityService.executeStatistic(input.getStatistic(), input.getCriteria(), input.getUserId());
            } else if (input.getCriteria() == ECriteria.AREA) {
                return statisticPriceByAreaService.executeStatistic(input.getStatistic(), input.getCriteria(), input.getUserId());
            }
        }
        throw new InvalidException(messageHelper.getMessage(MessageCode.System.INVALID));
    }
}
