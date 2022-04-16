package com.uit.realestate.payload.scraper;

import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RawDataScraper {

    private String title;
    private String floors;
    private String bedrooms;
    private String areas;
    private String direction;
    private ETypeApartment type;
    private String link;
    private String price; // need to check type buy or rent
    private String address;
    private String district;
    private String province;
}
