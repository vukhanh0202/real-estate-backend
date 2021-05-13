package com.uit.realestate.controller;

import com.uit.realestate.dto.response.ApiResponse;
import com.uit.realestate.service.location.ILocationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/location")
@Api(value = "Location APIs")
public class LocationController {

    @Autowired
    private ILocationService locationService;

    @ApiOperation(value = "Find All Country")
    @GetMapping(value = "/country")
    public ResponseEntity<?> findAllCountry(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(locationService.getFindAllCountryService()
                        .execute()));
    }

    @ApiOperation(value = "Find All province by country code")
    @GetMapping(value = "/province/country")
    public ResponseEntity<?> findAllProvinceByCountryCode(@RequestParam(value = "country_code", defaultValue = "VN") String countryCode){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(locationService.getFindAllProvinceByCountryCodeService()
                        .execute(countryCode)));
    }

    @ApiOperation(value = "Find All district by province id")
    @GetMapping(value = "/district/province")
    public ResponseEntity<?> findAllDistrictByProvinceId(@RequestParam(value = "province_id") Long provinceId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(locationService.getFindAllDistrictByProvinceIdService()
                        .execute(provinceId)));
    }
}
