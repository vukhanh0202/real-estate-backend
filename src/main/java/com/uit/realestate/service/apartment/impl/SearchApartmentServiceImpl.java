package com.uit.realestate.service.apartment.impl;

import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.dto.apartment.ApartmentDto;
import com.uit.realestate.dto.response.PaginationResponse;
import com.uit.realestate.mapper.apartment.ApartmentMapper;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.apartment.ISearchApartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SearchApartmentServiceImpl extends AbstractBaseService<ISearchApartmentService.Input, PaginationResponse<ApartmentDto>>
        implements ISearchApartmentService<ISearchApartmentService.Input, PaginationResponse<ApartmentDto>> {

    @Autowired
    ApartmentMapper apartmentMapper;

    @Autowired
    ApartmentRepository apartmentRepository;

    @Override
    public PaginationResponse<ApartmentDto> doing(Input input) {
        log.info("Search Apartment");
        Page<Apartment> result = apartmentRepository.findAll(input.getPageable());
        return new PaginationResponse(
                result.getTotalElements()
                , result.getNumberOfElements()
                , result.getNumber() + 1
                , apartmentMapper.toApartmentPreviewDtoList(result.getContent()));
    }
}
