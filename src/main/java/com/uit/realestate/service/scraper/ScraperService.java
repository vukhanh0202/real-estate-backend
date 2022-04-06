package com.uit.realestate.service.scraper;

import com.uit.realestate.dto.scraper.LinkDto;

import java.util.List;

public interface ScraperService {
    List<LinkDto> scrapingData(int size);
}
