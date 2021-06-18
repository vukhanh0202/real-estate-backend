package com.uit.realestate.controller.dashboard;


import com.uit.realestate.constant.AppConstant;
import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.constant.enums.sort.ESortApartment;
import com.uit.realestate.data.UserPrincipal;
import com.uit.realestate.dto.response.ApiResponse;
import com.uit.realestate.payload.apartment.AddApartmentRequest;
import com.uit.realestate.payload.apartment.UpdateApartmentRequest;
import com.uit.realestate.service.apartment.IApartmentService;
import com.uit.realestate.service.apartment.ISearchApartmentService;
import com.uit.realestate.service.apartment.IValidateApartmentService;
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

@RestController
@Slf4j
@Api(value = "Apartment Dashboard APIs")
@RequestMapping("/dashboard/apartment")
public class DashboardApartmentController {

    @Autowired
    private IApartmentService apartmentService;

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
                                              @RequestParam(value = "status",  required = false) EApartmentStatus status,
                                              @RequestParam(value = "search", defaultValue = "") String search
                                              ) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ISearchApartmentService.Input input = new ISearchApartmentService.Input(page, size, districtId, provinceId,
                priceFrom, priceTo, areaFrom, areaTo, categoryId, typeApartment, status, userPrincipal.getId(), search);
        input.createPageable(sortDirection, sortBy.getValue());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.getSearchApartmentService()
                        .execute(input)));
    }

    /**
     * Add new apartment
     *
     * @param addApartmentRequest
     * @return
     */
    @ApiOperation(value = "Add new apartment(auto open)", authorizations = {@Authorization(value = "JWT")})
    @PostMapping(value = "/create")
    public ResponseEntity<?> addNewApartment(@RequestBody AddApartmentRequest addApartmentRequest) {
        addApartmentRequest.setStatus(EApartmentStatus.OPEN);
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
    @PutMapping(value = "/{id}/update")
    public ResponseEntity<?> updateApartment(@PathVariable("id") Long id,
                                             @RequestBody UpdateApartmentRequest updateApartmentRequest) {
        updateApartmentRequest.setIsAdmin(true);
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        updateApartmentRequest.setAuthorId(userPrincipal.getId());
        updateApartmentRequest.setId(id);
        updateApartmentRequest.setIsAdmin(true);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.getUpdateApartmentService()
                        .execute(updateApartmentRequest)));
    }


    /**
     * Approve/Decline apartment
     *
     * @param decision
     * @return
     */
    @ApiOperation(value = "Validate apartment", authorizations = {@Authorization(value = "JWT")})
    @PostMapping(value = "/{id}/validate")
    public ResponseEntity<?> validateApartment(@PathVariable("id") Long id,
                                               @RequestParam(value = "decision") Boolean decision) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.getValidateApartmentService()
                        .execute(new IValidateApartmentService.Input(id, decision))));
    }

    /**
     * Highlight apartment
     *
     * @return
     */
    @ApiOperation(value = "Highlight apartment", authorizations = {@Authorization(value = "JWT")})
    @PostMapping(value = "/validate/highlight/{id}/")
    public ResponseEntity<?> highlightApartment(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(apartmentService.getHighlightApartmentService()
                        .execute(id)));
    }
}
