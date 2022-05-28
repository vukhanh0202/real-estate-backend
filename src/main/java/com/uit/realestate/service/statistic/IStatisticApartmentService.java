package com.uit.realestate.service.statistic;

import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.constant.enums.statistic.ECriteria;
import com.uit.realestate.constant.enums.statistic.EStatistic;
import com.uit.realestate.domain.apartment.ApartmentRating;
import com.uit.realestate.dto.apartment.ApartmentBasicDto;
import com.uit.realestate.dto.statistic.StatisticDto;
import com.uit.realestate.mapper.apartment.ApartmentMapper;
import com.uit.realestate.payload.apartment.SearchApartmentRequest;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public abstract class IStatisticApartmentService {

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private ApartmentMapper apartmentMapper;

    public abstract StatisticDto executeStatistic(EStatistic statistic, ECriteria criteria, Long userId , String ip, ETypeApartment typeApartment);

    protected List<ApartmentBasicDto> getSuitableApartment(SearchApartmentRequest searchApartmentRequest, Long userId, String ip) {
        searchApartmentRequest.setApartmentStatus(EApartmentStatus.OPEN.name());
        List<ApartmentRating> result = apartmentRepository
                .findRecommendApartmentByUserIdAndIp(searchApartmentRequest, userId, ip,
                        PageRequest.of(0, 3, JpaSort.unsafe(Sort.Direction.DESC, "(rating)")));
        return apartmentMapper.toApartmentBasicRatingDtoList(result, userId, ip);
    }
}
