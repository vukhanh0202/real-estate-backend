package com.uit.realestate.service.location.impl;

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
public class LocationServiceImpl implements ILocationService {

    @Autowired
    private IFindAllCountryService findAllCountryService;

    @Autowired
    private IFindAllProvinceByCountryCodeService findAllProvinceByCountryCodeService;

    @Autowired
    private IFindAllDistrictByProvinceIdService findAllDistrictByProvinceIdService;
}
