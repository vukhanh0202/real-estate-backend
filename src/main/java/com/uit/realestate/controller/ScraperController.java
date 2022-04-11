package com.uit.realestate.controller;

import com.uit.realestate.constant.enums.EScraper;
import com.uit.realestate.dto.response.ApiResponse;
import com.uit.realestate.service.scraper.ScraperServiceFactory;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("public/scraper")
@Api(value = "Scraper APIs")
@RequiredArgsConstructor
public class ScraperController {

    private final ScraperServiceFactory scraperServiceFactory;

    @GetMapping(path = "/")
    public ResponseEntity<?> getVehicleByModel() {
        scraperServiceFactory.getScraperService(EScraper.PROPZY).scrapingData(1000);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(true));
    }
}
