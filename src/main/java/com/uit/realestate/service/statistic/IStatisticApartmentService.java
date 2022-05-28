package com.uit.realestate.service.statistic;

import com.uit.realestate.constant.SuitabilityConstant;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.constant.enums.statistic.ECriteria;
import com.uit.realestate.constant.enums.statistic.EStatistic;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.dto.apartment.ApartmentBasicDto;
import com.uit.realestate.dto.statistic.StatisticDto;
import com.uit.realestate.mapper.apartment.ApartmentMapper;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public abstract class IStatisticApartmentService {

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private ApartmentMapper apartmentMapper;

    public abstract StatisticDto executeStatistic(EStatistic statistic, ECriteria criteria, Long userId , String ip, ETypeApartment typeApartment);

    protected List<ApartmentBasicDto> getSuitableApartment(List<Apartment> apartments, Long userId, String ip) {
        List<ApartmentBasicDto> apartmentBasicDtoList = new ArrayList<>();
        if (userId != null) {
            List<Apartment> list = apartmentRepository
                    .findApartmentWithSuitableDesc(SuitabilityConstant.DEFAULT_ACCURACY,
                            SuitabilityConstant.DEFAULT_ACCURACY_AREA, userId, 0L, 3L);
            apartmentBasicDtoList = apartmentMapper.toApartmentBasicDtoList(list, userId, ip);
        }
        if (userId == null || apartmentBasicDtoList.size() < 3) {
            Collections.shuffle(apartments);
            if (apartments.size() > 2) {
                apartmentBasicDtoList = apartmentMapper.toApartmentBasicDtoList(apartments.subList(0, 3), -1L, ip);
            } else {
                apartmentBasicDtoList = apartmentMapper.toApartmentBasicDtoList(apartments, -1L, ip);
            }
        }
        return apartmentBasicDtoList;
    }

    ;
}
