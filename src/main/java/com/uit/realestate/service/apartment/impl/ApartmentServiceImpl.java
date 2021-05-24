package com.uit.realestate.service.apartment.impl;

import com.uit.realestate.service.apartment.*;
import com.uit.realestate.service.location.IFindAllCountryService;
import com.uit.realestate.service.location.IFindAllDistrictByProvinceIdService;
import com.uit.realestate.service.location.IFindAllProvinceByCountryCodeService;
import com.uit.realestate.service.location.ILocationService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
@Slf4j
public class ApartmentServiceImpl implements IApartmentService {

    @Autowired
    private ISearchApartmentService searchApartmentService;

    @Autowired
    private IGetApartmentDetailService getApartmentDetailService;

    @Autowired
    private IAddApartmentService addApartmentService;

    @Autowired
    private IFindLatestNewApartmentService findLatestNewApartmentService;
}
