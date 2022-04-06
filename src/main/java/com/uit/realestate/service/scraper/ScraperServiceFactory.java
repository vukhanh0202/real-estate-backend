package com.uit.realestate.service.scraper;

import com.uit.realestate.constant.enums.EScraper;

public interface ScraperServiceFactory {
    ScraperService getScraperService(EScraper scraper);
}
