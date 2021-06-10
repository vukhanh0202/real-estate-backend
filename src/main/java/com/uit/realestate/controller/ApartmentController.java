package com.uit.realestate.controller;

import com.uit.realestate.constant.AppConstant;
import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.constant.enums.sort.ESortApartment;
import com.uit.realestate.data.UserPrincipal;
import com.uit.realestate.dto.response.ApiResponse;
import com.uit.realestate.payload.apartment.AddApartmentRequest;
import com.uit.realestate.payload.apartment.UpdateApartmentRequest;
import com.uit.realestate.repository.tracking.TrackingDistrictRepository;
import com.uit.realestate.repository.tracking.TrackingProvinceRepository;
import com.uit.realestate.service.apartment.IApartmentService;
import com.uit.realestate.service.apartment.ISearchApartmentService;
import com.uit.realestate.service.apartment.IValidateApartmentService;
import com.uit.realestate.service.tracking.ITrackingService;
import com.uit.realestate.service.tracking.TrackingCategoryService;
import com.uit.realestate.service.tracking.TrackingDistrictService;
import com.uit.realestate.service.tracking.TrackingProvinceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@Api(value = "Apartment APIs")
public class ApartmentController {

    @Autowired
    private IApartmentService apartmentService;

    @Autowired
    private TrackingCategoryService trackingCategoryService;

    @Autowired
    private TrackingDistrictService trackingDistrictService;

    @Autowired
    private TrackingProvinceService trackingProvinceService;

    /**
     * Search apartment
     *
     * @param page
     * @param size
     * @param sortBy
     * @param sortDirection
     * @param districtId
     * @param provinceId
     * @param priceFrom
     * @param priceTo
     * @param areaFrom
     * @param areaTo
     * @param categoryId
     * @param typeApartment
     * @return
     */
    @ApiOperation(value = "Search apartment")
    @GetMapping(value = "/public/apartment/search")
    public ResponseEntity<?> findAllApartment(@RequestParam(value = "page", defaultValue = AppConstant.PAGE_NUMBER_DEFAULT) Integer page,
                                              @RequestParam(value = "size", defaultValue = AppConstant.PAGE_SIZE_DEFAULT) Integer size,
                                              @RequestParam(value = "sort_by", defaultValue = "ID") ESortApartment sortBy,
                                              @RequestParam(value = "sort_direction", defaultValue = "DESC") Sort.Direction sortDirection,
                                              @RequestParam(value = "district_id", required = false) Long districtId,
                                              @RequestParam(value = "province_id", required = false) Long provinceId,
                                              @RequestParam(value = "price_from", required = false) Double priceFrom,
                                              @RequestParam(value = "price_to", required = false) Double priceTo,
                                              @RequestParam(value = "area_from", required = false) Double areaFrom,
                                              @RequestParam(value = "area_to", required = false) Double areaTo,
                                              @RequestParam(value = "category_id", required = false) Long categoryId,
                                              @RequestParam(value = "type_apartment") ETypeApartment typeApartment,
                                              HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            userId = ((UserPrincipal) authentication.getPrincipal()).getId();
        }
        log.info("Tracking User");
        trackingCategoryService.tracking(userId, request.getRemoteAddr(), categoryId, AppConstant.DEFAULT_RATING);
        trackingDistrictService.tracking(userId, request.getRemoteAddr(), districtId, AppConstant.DEFAULT_RATING);
        trackingProvinceService.tracking(userId, request.getRemoteAddr(), provinceId, AppConstant.DEFAULT_RATING);

        ISearchApartmentService.Input input = new ISearchApartmentService.Input(page, size, districtId, provinceId,
                priceFrom, priceTo, areaFrom, areaTo, categoryId, typeApartment);
        input.createPageable(sortDirection, sortBy.getValue());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.getSearchApartmentService()
                        .execute(input)));
    }

    /**
     * Get latest new apartment (4 items)
     *
     * @return
     */
    @ApiOperation(value = "Get latest new apartment ")
    @GetMapping(value = "/public/apartment/latest-new")
    public ResponseEntity<?> findLatestNewApartment() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.getFindLatestNewApartmentService()
                        .execute()));
    }

    /**
     * Get hightlight apartment (4 items)
     *
     * @return
     */
    @ApiOperation(value = "Get highlight apartment ")
    @GetMapping(value = "/public/apartment/highlight")
    public ResponseEntity<?> findHighlightApartment() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.getFindHighlightApartmentService()
                        .execute()));
    }

    /**
     * Get detail apartment
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "Get apartment detail")
    @GetMapping(value = "/public/apartment/{id}")
    public ResponseEntity<?> findAllCategory(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.getGetApartmentDetailService()
                        .execute(id)));
    }

    /**
     * Add new apartment
     *
     * @param addApartmentRequest
     * @return
     */
    @ApiOperation(value = "Add new apartment", authorizations = {@Authorization(value = "JWT")})
    @PostMapping(value = "/apartment/create")
    public ResponseEntity<?> addNewApartment(@RequestBody AddApartmentRequest addApartmentRequest) {
        addApartmentRequest.setStatus(EApartmentStatus.PENDING);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.getAddApartmentService()
                        .execute(addApartmentRequest)));
    }

    /**
     * Update apartment
     *
     * @param updateApartmentRequest
     * @return
     */
    @ApiOperation(value = "Update apartment", authorizations = {@Authorization(value = "JWT")})
    @PutMapping(value = "/apartment/{id}/update")
    public ResponseEntity<?> updateApartment(@PathVariable("id") Long id,
                                             @RequestBody UpdateApartmentRequest updateApartmentRequest) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        updateApartmentRequest.setAuthorId(userPrincipal.getId());
        updateApartmentRequest.setId(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.getUpdateApartmentService()
                        .execute(updateApartmentRequest)));
    }

    /**
     * Update apartment
     *
     * @return
     */
    @ApiOperation(value = "Close apartment", authorizations = {@Authorization(value = "JWT")})
    @PutMapping(value = "/apartment/{id}/close/")
    public ResponseEntity<?> cancelApartment(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.getCloseApartmentService()
                        .execute(id)));
    }

    /**
     * Approve/Decline apartment
     *
     * @param decision
     * @return
     */
    @ApiOperation(value = "Validate apartment", authorizations = {@Authorization(value = "JWT")})
    @PostMapping(value = "/apartment/{id}/validate")
    public ResponseEntity<?> validateApartment(@PathVariable("id") Long id,
                                               @RequestParam(value = "decision") Boolean decision) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.getValidateApartmentService()
                        .execute(new IValidateApartmentService.Input(id, decision))));
    }

}
