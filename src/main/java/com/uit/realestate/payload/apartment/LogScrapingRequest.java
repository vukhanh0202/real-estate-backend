package com.uit.realestate.payload.apartment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogScrapingRequest {

    private Long idApartment;
    private String titleApartment;
    private String linkScraping;
    private boolean status;
    private String error;
}
