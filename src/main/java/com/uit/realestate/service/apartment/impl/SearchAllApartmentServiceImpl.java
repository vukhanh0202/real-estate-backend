package com.uit.realestate.service.apartment.impl;

import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.dto.apartment.ApartmentDto;
import com.uit.realestate.dto.apartment.ApartmentSearchDto;
import com.uit.realestate.dto.response.PaginationResponse;
import com.uit.realestate.mapper.apartment.ApartmentMapper;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.apartment.ISearchAllApartmentService;
import com.uit.realestate.service.apartment.ISearchApartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchAllApartmentServiceImpl extends AbstractBaseService<String, List<ApartmentSearchDto>>
        implements ISearchAllApartmentService {

    private final ApartmentMapper apartmentMapper;

    private final ApartmentRepository apartmentRepository;

    @Override
    public List<ApartmentSearchDto> doing(String search) {
        log.info("Search All Apartment");
        List<Apartment> result = apartmentRepository.findAllByStatusAndTitleContainingIgnoreCase(EApartmentStatus.OPEN, search);
        return apartmentMapper.toApartmentSearchDtoList(result);
    }

}
