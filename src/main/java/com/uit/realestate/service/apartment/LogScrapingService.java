package com.uit.realestate.service.apartment;

import com.uit.realestate.payload.apartment.LogScrapingRequest;

public interface LogScrapingService {

    boolean save(LogScrapingRequest req);
}
