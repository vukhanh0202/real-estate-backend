package com.uit.realestate.controller;

import com.uit.realestate.constant.AppConstant;
import com.uit.realestate.constant.enums.ETrackingType;
import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.constant.enums.sort.ESortApartment;
import com.uit.realestate.data.UserPrincipal;
import com.uit.realestate.dto.response.ApiResponse;
import com.uit.realestate.payload.CatchInfoRequestExt;
import com.uit.realestate.payload.apartment.*;
import com.uit.realestate.service.apartment.ApartmentService;
import com.uit.realestate.service.tracking.TrackingService;
import com.uit.realestate.utils.IPUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@Api(value = "Apartment APIs")
@RequiredArgsConstructor
public class ApartmentController {

    private final ApartmentService apartmentService;

    private final TrackingService trackingService;

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
                                                  @RequestParam(value = "sort_by", defaultValue = "RATING") ESortApartment sortBy,
                                                  @RequestParam(value = "sort_direction", defaultValue = "DESC") Sort.Direction sortDirection,
                                                  @RequestParam(value = "district_id", required = false) Long districtId,
                                                  @RequestParam(value = "province_id", required = false) Long provinceId,
                                                  @RequestParam(value = "price_from", required = false) Double priceFrom,
                                                  @RequestParam(value = "price_to", required = false) Double priceTo,
                                                  @RequestParam(value = "area_from", required = false) Double areaFrom,
                                                  @RequestParam(value = "area_to", required = false) Double areaTo,
                                                  @RequestParam(value = "category_id", required = false) Long categoryId,
                                                  @RequestParam(value = "type_apartment", defaultValue = "BUY") ETypeApartment typeApartment,
                                                  @RequestParam(value = "user_id", defaultValue = "-1") Long userId,
                                                  @RequestParam(value = "house_direction", required = false) String houseDirection,
                                                  @RequestParam(value = "bedroom_quantity", required = false) Long bedroomQuantity,
                                                  @RequestParam(value = "bathroom_quantity", required = false) Long bathroomQuantity,
                                                  @RequestParam(value = "floor_quantity", required = false) Long floorQuantity,
                                                  @RequestParam(value = "search", defaultValue = "") String search,
                                                  HttpServletRequest request) {
        log.info("Tracking User");
        Map<ETrackingType, String> map = new HashMap<>();
        map.put(ETrackingType.CATEGORY, String.valueOf(categoryId));
        map.put(ETrackingType.DISTRICT, String.valueOf(districtId));
        map.put(ETrackingType.PROVINCE, String.valueOf(provinceId));
        map.put(ETrackingType.TYPE, typeApartment.name());
        map.put(ETrackingType.BATHROOM, String.valueOf(bathroomQuantity));
        map.put(ETrackingType.BEDROOM, String.valueOf(bedroomQuantity));
        map.put(ETrackingType.DIRECTION, houseDirection);
        map.put(ETrackingType.FLOOR, String.valueOf(floorQuantity));
        trackingService.tracking(userId, IPUtils.getIp(request), map, AppConstant.DEFAULT_RATING);

        SearchApartmentRequest req = new SearchApartmentRequest(page, size, districtId, provinceId,
                priceFrom, priceTo, areaFrom, areaTo, categoryId, typeApartment.name(), EApartmentStatus.OPEN, userId, IPUtils.getIp(request), search,
                houseDirection, bedroomQuantity, bathroomQuantity, floorQuantity);
        if (sortBy.getValue().equals("rating")) {
            req.createPageable(JpaSort.unsafe(sortDirection, "(rating)"));
        } else {
            if (sortBy.equals(ESortApartment.TOTAL_PRICE)){
                if (typeApartment != null && typeApartment.equals(ETypeApartment.BUY)){
                    req.createPageable(Sort.by(sortDirection, sortBy.getValue()).and(JpaSort.unsafe(Sort.Direction.DESC, "(rating)")));
                }else{
                    req.createPageable(Sort.by(sortDirection, "price_rent").and(JpaSort.unsafe(Sort.Direction.DESC, "(rating)")));
                }
            }else{
                req.createPageable(Sort.by(sortDirection, sortBy.getValue()).and(JpaSort.unsafe(Sort.Direction.DESC, "(rating)")));
            }
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.searchApartment(req)));
    }

    /**
     * Search all apartment no pagination
     *
     * @param search
     * @return
     */
    @ApiOperation(value = "Search apartment")
    @GetMapping(value = "/public/apartment/search/all")
    public ResponseEntity<?> findAllApartmentOpen(@RequestParam(value = "search", defaultValue = "") String search) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.searchAllApartment(search)));
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
                                                    @RequestParam(value = "type_apartment", defaultValue = "BUY") ETypeApartment typeApartment,
                                                    @RequestParam(value = "user_id", defaultValue = "-1") Long userId,
                                                    HttpServletRequest request) {
        RecommendApartmentRequest req = new RecommendApartmentRequest(userId, IPUtils.getIp(request), page, size, typeApartment);
        req.createPageable();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.findRecommendApartment(req)));
    }

//    /**
//     * Get similar apartment
//     *
//     * @return
//     */
//    @ApiOperation(value = "Get similar apartment ")
//    @GetMapping(value = "/public/apartment/similar")
//    public ResponseEntity<?> findSimilarApartment(@RequestParam(value = "page", defaultValue = AppConstant.PAGE_NUMBER_DEFAULT) Integer page,
//                                                  @RequestParam(value = "size", defaultValue = AppConstant.PAGE_SIZE_DEFAULT) Integer size,
//                                                  @RequestParam(value = "user_id", defaultValue = "-1") Long userId,
//                                                  HttpServletRequest request) {
//
//        CatchInfoRequestExt req = new CatchInfoRequestExt(userId, IPUtils.getIp(request), page, size);
//        req.createPageable();
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(new ApiResponse(apartmentService.findSimilarApartment(req)));
//    }

    /**
     * Get latest new apartment (4 items)
     *
     * @return
     */
    @ApiOperation(value = "Get latest new apartment ")
    @GetMapping(value = "/public/apartment/latest-new")
    public ResponseEntity<?> findLatestNewApartment(@RequestParam(value = "type_apartment", defaultValue = "BUY") ETypeApartment typeApartment,
                                                    @RequestParam(value = "user_id", defaultValue = "-1") Long userId,
                                                    HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.findLatestApartment(new LatestApartmentRequest(userId, IPUtils.getIp(request), typeApartment))));
    }

    /**
     * Get hightlight apartment (4 items)
     *
     * @return
     */
    @ApiOperation(value = "Get highlight apartment ")
    @GetMapping(value = "/public/apartment/highlight")
    public ResponseEntity<?> findHighlightApartment(@RequestParam(value = "type_apartment", defaultValue = "BUY") ETypeApartment typeApartment,
                                                    @RequestParam(value = "province_id") Long provinceId,
                                                    @RequestParam(value = "user_id", defaultValue = "-1") Long userId,
                                                    HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.findHighLightApartment(new HighlightApartmentRequest(userId, IPUtils.getIp(request), typeApartment, provinceId))));
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
                                                 @RequestParam(value = "user_id", defaultValue = "-1") Long userId,
                                                 HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.getApartmentDetail(new DetailApartmentRequest(id, IPUtils.getIp(request), userId))));
    }

    /**
     * Add new apartment
     *
     * @param addApartmentRequest
     * @return
     */
    @ApiOperation(value = "Add new apartment", authorizations = {@Authorization(value = "JWT")})
    @PostMapping(value = "/apartment/create")
    @PreAuthorize("@securityService.hasRoles('USER', 'ADMIN')")
    public ResponseEntity<?> addNewApartment(@RequestBody AddApartmentRequest addApartmentRequest) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        addApartmentRequest.setStatus(EApartmentStatus.PENDING);
        addApartmentRequest.setAuthorId(userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.addApartment(addApartmentRequest)));
    }

    /**
     * Update apartment
     *
     * @param updateApartmentRequest
     * @return
     */
    @ApiOperation(value = "Update apartment", authorizations = {@Authorization(value = "JWT")})
    @PutMapping(value = "/apartment/{id}/update")
    @PreAuthorize("@securityService.hasRoles('USER', 'ADMIN')")
    public ResponseEntity<?> updateApartment(@PathVariable("id") Long id,
                                             @RequestBody UpdateApartmentRequest updateApartmentRequest) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        updateApartmentRequest.setId(id);
        updateApartmentRequest.setAuthorId(userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.updateApartment(updateApartmentRequest)));
    }

    /**
     * Delete apartment
     *
     * @return
     */
    @ApiOperation(value = "Delete apartment", authorizations = {@Authorization(value = "JWT")})
    @DeleteMapping(value = "/delete/{id}/")
    @PreAuthorize("@securityService.hasRoles('USER', 'ADMIN')")
    public ResponseEntity<?> deleteApartmentApartment(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.closeApartmentService(id)));
    }

    /**
     * Favourite apartment
     *
     * @return
     */
    @ApiOperation(value = "Favourite apartment", authorizations = {@Authorization(value = "JWT")})
    @PostMapping(value = "/apartment/{apartmentId}/favourite")
    @PreAuthorize("@securityService.hasRoles('USER', 'ADMIN')")
    public ResponseEntity<?> favouriteApartment(@PathVariable("apartmentId") Long apartmentId,
                                                HttpServletRequest request) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService
                        .favouriteApartment(new FavouriteApartmentRequest(userPrincipal.getId(), apartmentId, IPUtils.getIp(request)))));
    }

    @ApiOperation(value = "Compare apartment")
    @GetMapping(value = "/public/apartment/compare")
    public ResponseEntity<?> compareApartment(@RequestParam(value = "apartment_list") List<Long> apartmentIds,
                                              @RequestParam(value = "user_id", defaultValue = "-1") Long userId,
                                              HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.compareApartment(new CompareApartmentRequest(apartmentIds, userId, IPUtils.getIp(request)))));
    }
}
