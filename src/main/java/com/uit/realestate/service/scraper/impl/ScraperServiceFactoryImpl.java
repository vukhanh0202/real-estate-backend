package com.uit.realestate.service.scraper.impl;

import com.uit.realestate.constant.enums.EScraper;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.service.scraper.ScraperService;
import com.uit.realestate.service.scraper.ScraperServiceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ScraperServiceFactoryImpl implements ScraperServiceFactory {

    @Value("${scrapingUrl-propzy}")
    private String propzyUrl;

    @Override
    public ScraperService getScraperService(EScraper scraper) {
        switch (scraper){
            case PROPZY: return new ScraperPropzyServiceImpl(propzyUrl);
        }
        throw new NotFoundException("Not Found");
    }
}
