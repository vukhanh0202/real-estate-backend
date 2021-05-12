package com.uit.realestate.controller;

import com.uit.realestate.dto.response.ApiResponse;
import com.uit.realestate.service.location.ILocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/location")
public class LocationController {

    @Autowired
    private ILocationService locationService;

    @GetMapping(value = "/country")
    public ResponseEntity<?> findAllCountry(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(locationService.getFindAllCountryService()
                        .execute()));
    }

    @GetMapping(value = "/province/country")
    public ResponseEntity<?> findAllProvinceByCountryCode(@RequestParam(value = "country_code", defaultValue = "VN") String countryCode){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(locationService.getFindAllProvinceByCountryCodeService()
                        .execute(countryCode)));
    }

    @GetMapping(value = "/district/province")
    public ResponseEntity<?> findAllDistrictByProvinceId(@RequestParam(value = "province_id") Long provinceId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(locationService.getFindAllDistrictByProvinceIdService()
                        .execute(provinceId)));
    }
}
