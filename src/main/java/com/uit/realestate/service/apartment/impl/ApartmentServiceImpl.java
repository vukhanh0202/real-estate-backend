package com.uit.realestate.service.apartment.impl;

import com.uit.realestate.service.apartment.*;
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

    private final IValidateApartmentService validateApartmentService;

    private final IUpdateApartmentService updateApartmentService;

    private final ICloseApartmentService closeApartmentService;

    private final IFindRecommendApartmentService findRecommendApartmentService;

    private final IFavouriteApartmentService favouriteApartmentService;

    private final IHighlightApartmentService highlightApartmentService;

    public ApartmentServiceImpl(ISearchApartmentService searchApartmentService, IGetApartmentDetailService getApartmentDetailService, IAddApartmentService addApartmentService, IFindLatestNewApartmentService findLatestNewApartmentService, IFindHighlightApartmentService findHighlightApartmentService, IValidateApartmentService validateApartmentService, IUpdateApartmentService updateApartmentService, ICloseApartmentService closeApartmentService, IFindRecommendApartmentService findRecommendApartmentService, IFavouriteApartmentService favouriteApartmentService, IHighlightApartmentService highlightApartmentService) {
        this.searchApartmentService = searchApartmentService;
        this.getApartmentDetailService = getApartmentDetailService;
        this.addApartmentService = addApartmentService;
        this.findLatestNewApartmentService = findLatestNewApartmentService;
        this.findHighlightApartmentService = findHighlightApartmentService;
        this.validateApartmentService = validateApartmentService;
        this.updateApartmentService = updateApartmentService;
        this.closeApartmentService = closeApartmentService;
        this.findRecommendApartmentService = findRecommendApartmentService;
        this.favouriteApartmentService = favouriteApartmentService;
        this.highlightApartmentService = highlightApartmentService;
    }
}
