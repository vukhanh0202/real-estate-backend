package com.uit.realestate.controller.dashboard;


import com.uit.realestate.constant.AppConstant;
import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.constant.enums.sort.ESortApartment;
import com.uit.realestate.data.UserPrincipal;
import com.uit.realestate.dto.response.ApiResponse;
import com.uit.realestate.payload.apartment.AddApartmentRequest;
import com.uit.realestate.payload.apartment.SearchApartmentRequest;
import com.uit.realestate.payload.apartment.UpdateApartmentRequest;
import com.uit.realestate.payload.apartment.ValidateApartmentRequest;
import com.uit.realestate.service.apartment.*;
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

@RestController
@Slf4j
@Api(value = "Apartment Dashboard APIs")
@RequestMapping("/dashboard/apartment")
@RequiredArgsConstructor
public class DashboardApartmentController {

    private final ApartmentService apartmentService;

    /**
     * Search apartment admin
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
    @ApiOperation(value = "Search apartment", authorizations = {@Authorization(value = "JWT")})
    @GetMapping(value = "/search")
    @PreAuthorize("@securityService.hasRoles('ADMIN')")
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
                                              @RequestParam(value = "type_apartment", required = false) ETypeApartment typeApartment,
                                              @RequestParam(value = "status", required = false) EApartmentStatus status,
                                              @RequestParam(value = "house_direction", required = false) String houseDirection,
                                              @RequestParam(value = "bedroom_quantity", required = false) Long bedroomQuantity,
                                              @RequestParam(value = "bathroom_quantity", required = false) Long bathroomQuantity,
                                              @RequestParam(value = "floor_quantity", required = false) Long floorQuantity,
                                              @RequestParam(value = "search", defaultValue = "") String search,
                                              HttpServletRequest request
    ) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SearchApartmentRequest req = new SearchApartmentRequest(page, size, districtId, provinceId,
                priceFrom, priceTo, areaFrom, areaTo, categoryId, typeApartment == null ? null : typeApartment.name(), status, userPrincipal.getId(), IPUtils.getIp(request), search,
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
     * Add new apartment
     *
     * @param addApartmentRequest
     * @return
     */
    @ApiOperation(value = "Add new apartment(auto open)", authorizations = {@Authorization(value = "JWT")})
    @PostMapping(value = "/create")
    @PreAuthorize("@securityService.hasRoles('ADMIN')")
    public ResponseEntity<?> addNewApartment(@RequestBody AddApartmentRequest addApartmentRequest) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        addApartmentRequest.setAuthorId(userPrincipal.getId());
        addApartmentRequest.setStatus(EApartmentStatus.OPEN);
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
    @PutMapping(value = "/{id}/update")
    @PreAuthorize("@securityService.hasRoles('ADMIN')")
    public ResponseEntity<?> updateApartment(@PathVariable("id") Long id,
                                             @RequestBody UpdateApartmentRequest updateApartmentRequest) {
        updateApartmentRequest.setIsAdmin(true);
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        updateApartmentRequest.setAuthorId(userPrincipal.getId());
        updateApartmentRequest.setId(id);
        updateApartmentRequest.setIsAdmin(true);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.updateApartment(updateApartmentRequest)));
    }


    /**
     * Approve/Decline apartment
     *
     * @param decision
     * @return
     */
    @ApiOperation(value = "Validate apartment", authorizations = {@Authorization(value = "JWT")})
    @PostMapping(value = "/{id}/validate")
    @PreAuthorize("@securityService.hasRoles('ADMIN')")
    public ResponseEntity<?> validateApartment(@PathVariable("id") Long id,
                                               @RequestParam(value = "decision") Boolean decision) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.validateApartment(new ValidateApartmentRequest(id, decision))));
    }

    /**
     * Highlight apartment
     *
     * @return
     */
    @ApiOperation(value = "Highlight apartment", authorizations = {@Authorization(value = "JWT")})
    @PostMapping(value = "/validate/highlight/{id}/")
    @PreAuthorize("@securityService.hasRoles('ADMIN')")
    public ResponseEntity<?> highlightApartment(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.highLightApartment(id)));
    }

    /**
     * Close apartment
     *
     * @return
     */
    @ApiOperation(value = "Close apartment", authorizations = {@Authorization(value = "JWT")})
    @PostMapping(value = "/close/{id}/")
    @PreAuthorize("@securityService.hasRoles('ADMIN')")
    public ResponseEntity<?> closeApartment(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.closeApartmentService(id)));
    }
}
