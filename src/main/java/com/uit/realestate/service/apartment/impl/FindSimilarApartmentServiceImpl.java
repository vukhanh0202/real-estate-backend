package com.uit.realestate.service.apartment.impl;

import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.dto.apartment.ApartmentBasicDto;
import com.uit.realestate.dto.response.PaginationResponse;
import com.uit.realestate.mapper.apartment.ApartmentMapper;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.apartment.IFindSimilarApartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FindSimilarApartmentServiceImpl extends AbstractBaseService<IFindSimilarApartmentService.Input, PaginationResponse<ApartmentBasicDto>>
        implements IFindSimilarApartmentService {

    private final ApartmentMapper apartmentMapper;

    private final ApartmentRepository apartmentRepository;

    @Override
    public PaginationResponse<ApartmentBasicDto> doing(Input input) {
        log.info("Find Similar apartment");
        Page<Apartment> result = apartmentRepository
                .findRecommendApartmentByUserIdAndIp(input.getUserId(), input.getIp(), input.getPageable());
        List<ApartmentBasicDto> contents = apartmentMapper.toApartmentBasicDtoList(result.getContent(), input.getUserId());
        Collections.shuffle(contents);
        return new PaginationResponse(
                result.getTotalElements()
                , result.getNumberOfElements()
                , result.getNumber() + 1
                , contents);
    }
}
