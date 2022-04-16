package com.uit.realestate.service.scraper.impl;

import com.uit.realestate.configuration.property.ApplicationProperties;
import com.uit.realestate.constant.enums.EScraper;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.service.apartment.ApartmentService;
import com.uit.realestate.service.apartment.LogScrapingService;
import com.uit.realestate.service.category.CategoryService;
import com.uit.realestate.service.file.UploadService;
import com.uit.realestate.service.location.DistrictService;
import com.uit.realestate.service.location.ProvinceService;
import com.uit.realestate.service.scraper.ScraperService;
import com.uit.realestate.service.scraper.ScraperServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScraperServiceFactoryImpl implements ScraperServiceFactory {

    @Value("${scrapingUrl-propzy}")
    private String propzyUrl;

    @Value("${scrapingUrl-alond}")
    private String alondUrl;

    private final ApartmentService apartmentService;

    private final DistrictService districtService;

    private final CategoryService categoryService;

    private final UploadService uploadService;

    private final LogScrapingService logScrapingService;

    private final ProvinceService provinceService;

    private final ApplicationProperties app;

    @Override
    public ScraperService getScraperService(EScraper scraper) {
        switch (scraper){
            case PROPZY: return new ScraperPropzyServiceImpl(propzyUrl, apartmentService, districtService, provinceService, categoryService, uploadService, logScrapingService, app);
            case ALOND: return new ScraperAloNhaDatImpl(alondUrl, apartmentService, districtService, provinceService, categoryService, uploadService, logScrapingService, app);
        }
        throw new NotFoundException("Not Found");
    }
}
