package com.uit.realestate.controller;

import com.uit.realestate.dto.response.ApiResponse;
import com.uit.realestate.service.location.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/location")
@Api(value = "Location APIs")
@RequiredArgsConstructor
public class LocationController {

    private final CountryService countryService;

    private final ProvinceService provinceService;

    private final DistrictService districtService;

    @ApiOperation(value = "Find All Country")
    @GetMapping(value = "/country")
    public ResponseEntity<?> findAllCountry(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(countryService.findAll()));
    }

    @ApiOperation(value = "Find All province by country code")
    @GetMapping(value = "/province/country")
    public ResponseEntity<?> findAllProvinceByCountryCode(@RequestParam(value = "country_code", defaultValue = "VN") String countryCode){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(provinceService.findAllProvinceByCountryCode(countryCode)));
    }

    @ApiOperation(value = "Find All district by province id")
    @GetMapping(value = "/district/province")
    public ResponseEntity<?> findAllDistrictByProvinceId(@RequestParam(value = "province_id") Long provinceId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(districtService.findAllDistrictByProvince(provinceId)));
    }
}
