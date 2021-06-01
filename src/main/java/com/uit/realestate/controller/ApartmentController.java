package com.uit.realestate.controller;

import com.uit.realestate.constant.AppConstant;
import com.uit.realestate.dto.response.ApiResponse;
import com.uit.realestate.dto.response.PaginationResponse;
import com.uit.realestate.payload.apartment.AddApartmentRequest;
import com.uit.realestate.payload.apartment.ApartmentSearch;
import com.uit.realestate.service.apartment.IApartmentService;
import com.uit.realestate.service.apartment.ISearchApartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Api(value = "Apartment APIs")
public class ApartmentController {

    @Autowired
    private IApartmentService apartmentService;

    @ApiOperation(value = "Search apartment")
    @PostMapping(value = "/public/apartment/search")
    public ResponseEntity<?> findAllCategory(@RequestParam(value = "page", defaultValue = AppConstant.PAGE_NUMBER_DEFAULT) Integer page,
                                             @RequestParam(value = "size", defaultValue = AppConstant.PAGE_SIZE_DEFAULT) Integer size,
                                             @RequestParam(value = "sort_by", defaultValue = "id") String sortBy,
                                             @RequestParam(value = "sort_direction", defaultValue = "DESC") Sort.Direction sortDirection,
                                             @RequestBody ApartmentSearch requests) {
        ISearchApartmentService.Input input = new ISearchApartmentService.Input(requests, page, size);
        input.createPageable(sortDirection, sortBy);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.getSearchApartmentService()
                        .execute(input)));
    }


    @ApiOperation(value = "Get latest new apartment ")
    @GetMapping(value = "/public/apartment/latest-new")
    public ResponseEntity<?> findLatestNewApartment() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.getFindLatestNewApartmentService()
                        .execute()));
    }

    @ApiOperation(value = "Get highlight apartment ")
    @GetMapping(value = "/public/apartment/highlight")
    public ResponseEntity<?> findHighlightApartment() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.getFindHighlightApartmentService()
                        .execute()));
    }

    @ApiOperation(value = "Get apartment detail")
    @GetMapping(value = "/public/apartment/{id}")
    public ResponseEntity<?> findAllCategory(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.getGetApartmentDetailService()
                        .execute(id)));
    }

    @ApiOperation(value = "Add new apartment" , authorizations = { @Authorization(value="JWT") })
    @PostMapping(value = "/add-apartment")
    public ResponseEntity<?> addNewApartment(@RequestBody AddApartmentRequest addApartmentRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.getAddApartmentService()
                        .execute(addApartmentRequest)));
    }

}
