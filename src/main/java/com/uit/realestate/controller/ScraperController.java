package com.uit.realestate.controller;

import com.uit.realestate.constant.enums.EScraper;
import com.uit.realestate.dto.response.ApiResponse;
import com.uit.realestate.service.apartment.ApartmentService;
import com.uit.realestate.service.scraper.ScraperServiceFactory;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
@RequestMapping("public/scraper")
@Api(value = "Scraper APIs")
@RequiredArgsConstructor
public class ScraperController {

    private final ScraperServiceFactory scraperServiceFactory;
    private final ApartmentService apartmentService;

    @GetMapping(path = "/")
    public ResponseEntity<?> scraping() {
        CompletableFuture.runAsync(() -> scraperServiceFactory.getScraperService(EScraper.ALOND).scrapingData(4000));
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(true));
    }

    @GetMapping(path = "/test")
    public ResponseEntity<?> test() {

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.findApartmentWithSuitable(2L)));
    }
}
