package com.uit.realestate.service.apartment.impl;

import com.uit.realestate.domain.apartment.LogScraping;
import com.uit.realestate.payload.apartment.LogScrapingRequest;
import com.uit.realestate.repository.apartment.LogScrapingRepository;
import com.uit.realestate.service.apartment.LogScrapingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogScrapingServiceImpl implements LogScrapingService {

    private final LogScrapingRepository scrapingRepository;

    @Override
    public boolean save(LogScrapingRequest req) {
        if(Objects.isNull(req)){
            return false;
        }
        LogScraping logScraping = new LogScraping();
        logScraping.setIdApartment(req.getIdApartment());
        logScraping.setTitleApartment(req.getTitleApartment());
        logScraping.setLinkScraping(req.getLinkScraping());
        logScraping.setStatus(req.isStatus());
        logScraping.setError(req.getError());
        scrapingRepository.save(logScraping);
        return true;
    }
}
