package com.uit.realestate.service.apartment.impl;

import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.dto.apartment.ApartmentBasicDto;
import com.uit.realestate.dto.response.PaginationResponse;
import com.uit.realestate.mapper.apartment.ApartmentMapper;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.apartment.IFindRecommendApartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FindRecommendApartmentServiceImpl extends AbstractBaseService<IFindRecommendApartmentService.Input, PaginationResponse<ApartmentBasicDto>>
        implements IFindRecommendApartmentService {

    private final ApartmentMapper apartmentMapper;

    private final ApartmentRepository apartmentRepository;

    @Override
    public PaginationResponse<ApartmentBasicDto> doing(Input input) {
        log.info("Find recommend apartment");
        Page<Apartment> result = apartmentRepository
                .findRecommendApartmentByUserIdAndIp(input.getUserId(), input.getIp(), input.getPageable());
        return new PaginationResponse(
                result.getTotalElements()
                , result.getNumberOfElements()
                , result.getNumber() + 1
                , apartmentMapper.toApartmentPreviewDtoList(result.getContent(), input.getUserId()));
    }
}
