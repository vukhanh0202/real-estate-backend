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

    private final ISearchApartmentService searchApartmentService;

    private final IGetApartmentDetailService getApartmentDetailService;

    private final IAddApartmentService addApartmentService;

    private final IFindLatestNewApartmentService findLatestNewApartmentService;

    private final IFindHighlightApartmentService findHighlightApartmentService;

    private final ICountSearchApartmentService countSearchApartmentService;

    private final IValidateApartmentService validateApartmentService;

    private final IUpdateApartmentService updateApartmentService;

    private final ICloseApartmentService closeApartmentService;

    public ApartmentServiceImpl(ISearchApartmentService searchApartmentService, IGetApartmentDetailService getApartmentDetailService, IAddApartmentService addApartmentService, IFindLatestNewApartmentService findLatestNewApartmentService, IFindHighlightApartmentService findHighlightApartmentService, ICountSearchApartmentService countSearchApartmentService, IValidateApartmentService validateApartmentService, IUpdateApartmentService updateApartmentService, ICloseApartmentService closeApartmentService) {
        this.searchApartmentService = searchApartmentService;
        this.getApartmentDetailService = getApartmentDetailService;
        this.addApartmentService = addApartmentService;
        this.findLatestNewApartmentService = findLatestNewApartmentService;
        this.findHighlightApartmentService = findHighlightApartmentService;
        this.countSearchApartmentService = countSearchApartmentService;
        this.validateApartmentService = validateApartmentService;
        this.updateApartmentService = updateApartmentService;
        this.closeApartmentService = closeApartmentService;
    }
}
