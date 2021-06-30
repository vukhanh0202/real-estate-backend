package com.uit.realestate.service.apartment.impl;

import com.uit.realestate.service.apartment.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
@Slf4j
@RequiredArgsConstructor
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

    private final IFindSimilarApartmentService findSimilarApartmentService;
}
