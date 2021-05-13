package com.uit.realestate.service.apartment.impl;

import com.uit.realestate.dto.apartment.ApartmentBasicDto;
import com.uit.realestate.dto.location.CountryDto;
import com.uit.realestate.mapper.apartment.ApartmentMapper;
import com.uit.realestate.mapper.location.CountryMapper;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.repository.location.CountryRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.apartment.ISearchApartmentService;
import com.uit.realestate.service.location.IFindAllCountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SearchApartmentServiceImpl extends AbstractBaseService<ISearchApartmentService.Input, List<ApartmentBasicDto>>
        implements ISearchApartmentService<ISearchApartmentService.Input, List<ApartmentBasicDto>> {

    @Autowired
    ApartmentMapper apartmentMapper;

    @Autowired
    ApartmentRepository apartmentRepository;

    @Override
    public List<ApartmentBasicDto> doing(Input input) {
        log.info("Search Apartment");
        return apartmentMapper.toApartmentBasicDtoList(apartmentRepository.findAll());
    }
}
