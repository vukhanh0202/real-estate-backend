package com.uit.realestate.controller;

import com.uit.realestate.constant.AppConstant;
import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.constant.enums.sort.ESortApartment;
import com.uit.realestate.data.UserPrincipal;
import com.uit.realestate.dto.response.ApiResponse;
import com.uit.realestate.payload.apartment.AddApartmentRequest;
import com.uit.realestate.payload.apartment.UpdateApartmentRequest;
import com.uit.realestate.service.apartment.*;
import com.uit.realestate.service.tracking.TrackingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@Slf4j
@Api(value = "Apartment APIs")
public class ApartmentController {

    @Autowired
    private IApartmentService apartmentService;

    @Autowired
    private TrackingService tracking;

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
     * @param userId
     * @return
     */
    @ApiOperation(value = "Search apartment")
    @GetMapping(value = "/public/apartment/search")
    public ResponseEntity<?> findAllApartmentOpen(@RequestParam(value = "page", defaultValue = AppConstant.PAGE_NUMBER_DEFAULT) Integer page,
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
                                                  @RequestParam(value = "user_id", required = false) Long userId,
                                                  HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if (request.getRemoteAddr().equals("0:0:0:0:0:0:0:1")) {
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        log.info("Tracking User");
        tracking.trackingCategory(userId, ip, categoryId, AppConstant.DEFAULT_RATING);
        tracking.trackingDistrict(userId, ip, districtId, AppConstant.DEFAULT_RATING);
        tracking.trackingProvince(userId, ip, provinceId, AppConstant.DEFAULT_RATING);

        ISearchApartmentService.Input input = new ISearchApartmentService.Input(page, size, districtId, provinceId,
                priceFrom, priceTo, areaFrom, areaTo, categoryId, typeApartment, EApartmentStatus.OPEN, userId);
        input.createPageable(sortDirection, sortBy.getValue());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.getSearchApartmentService()
                        .execute(input)));
    }


    /**
     * Get recommend apartment
     *
     * @return
     */
    @ApiOperation(value = "Get recommend apartment ")
    @GetMapping(value = "/public/apartment/recommend")
    public ResponseEntity<?> findRecommendApartment(@RequestParam(value = "page", defaultValue = AppConstant.PAGE_NUMBER_DEFAULT) Integer page,
                                                    @RequestParam(value = "size", defaultValue = AppConstant.PAGE_SIZE_DEFAULT) Integer size,
                                                    @RequestParam(value = "user_id", defaultValue = "-1") Long userId,
                                                    HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if (request.getRemoteAddr().equals("0:0:0:0:0:0:0:1")) {
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        IFindRecommendApartmentService.Input input = new IFindRecommendApartmentService.Input(page, size, userId, ip);
        input.createPageable();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.getFindRecommendApartmentService()
                        .execute(input)));
    }

    /**
     * Get latest new apartment (4 items)
     *
     * @return
     */
    @ApiOperation(value = "Get latest new apartment ")
    @GetMapping(value = "/public/apartment/latest-new")
    public ResponseEntity<?> findLatestNewApartment(@RequestParam(value = "user_id", required = false) Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.getFindLatestNewApartmentService()
                        .execute(userId)));
    }

    /**
     * Get hightlight apartment (4 items)
     *
     * @return
     */
    @ApiOperation(value = "Get highlight apartment ")
    @GetMapping(value = "/public/apartment/highlight")
    public ResponseEntity<?> findHighlightApartment(@RequestParam(value = "user_id", required = false) Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.getFindHighlightApartmentService()
                        .execute(userId)));
    }

    /**
     * Get detail apartment
     *
     * @param id
     * @param userId
     * @return
     */
    @ApiOperation(value = "Get apartment detail")
    @GetMapping(value = "/public/apartment/{id}")
    public ResponseEntity<?> findApartmentDetail(@PathVariable("id") Long id,
                                             @RequestParam(value = "user_id", required = false) Long userId,
                                             HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if (request.getRemoteAddr().equals("0:0:0:0:0:0:0:1")) {
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.getGetApartmentDetailService()
                        .execute(new IGetApartmentDetailService.Input(id, ip, userId))));
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
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        addApartmentRequest.setStatus(EApartmentStatus.PENDING);
        addApartmentRequest.setAuthorId(userPrincipal.getId());
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
        updateApartmentRequest.setId(id);
        updateApartmentRequest.setAuthorId(userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.getUpdateApartmentService()
                        .execute(updateApartmentRequest)));
    }

    /**
     * Close Apartment apartment
     *
     * @return
     */
//    @ApiOperation(value = "Close apartment", authorizations = {@Authorization(value = "JWT")})
//    @PutMapping(value = "/apartment/{id}/close/")
//    public ResponseEntity<?> cancelApartment(@PathVariable("id") Long id) {
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(new ApiResponse(apartmentService.getCloseApartmentService()
//                        .execute(id)));
//    }

    /**
     * Favourite apartment
     *
     * @return
     */
    @ApiOperation(value = "Favourite apartment", authorizations = {@Authorization(value = "JWT")})
    @PostMapping(value = "/apartment/{apartmentId}/favourite")
    public ResponseEntity<?> favouriteApartment(@PathVariable("apartmentId") Long apartmentId,
                                                HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if (request.getRemoteAddr().equals("0:0:0:0:0:0:0:1")) {
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.getFavouriteApartmentService()
                        .execute(new IFavouriteApartmentService.Input(userPrincipal.getId(), apartmentId, ip))));
    }
}
