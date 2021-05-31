package com.uit.realestate.service.apartment.impl;

import com.uit.realestate.payload.apartment.ApartmentSearch;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.apartment.ICountSearchApartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CountSearchApartmentServiceImpl extends AbstractBaseService<ApartmentSearch, Long>
        implements ICountSearchApartmentService<ApartmentSearch, Long> {

    @Autowired
    ApartmentRepository apartmentRepository;

    @Override
    public Long doing(ApartmentSearch apartmentSearch) {
        log.info("Count apartment");
        return apartmentRepository.count();
    }
}
