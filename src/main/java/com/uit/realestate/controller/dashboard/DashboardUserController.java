package com.uit.realestate.controller.dashboard;

import com.uit.realestate.constant.AppConstant;
import com.uit.realestate.constant.enums.sort.ESortApartment;
import com.uit.realestate.dto.response.ApiResponse;
import com.uit.realestate.payload.category.CategoryRequest;
import com.uit.realestate.service.category.ICategoryService;
import com.uit.realestate.service.user.IFindAllUserService;
import com.uit.realestate.service.user.IUserService;
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
@Api(value = "Category Dashboard APIs")
@RequestMapping("/dashboard/user")
public class DashboardUserController {

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "Find All User", authorizations = {@Authorization(value = "JWT")})
    @GetMapping(value = "/search")
    public ResponseEntity<?> findAllUser(@RequestParam(value = "page", defaultValue = AppConstant.PAGE_NUMBER_DEFAULT) Integer page,
                                         @RequestParam(value = "size", defaultValue = AppConstant.PAGE_SIZE_DEFAULT) Integer size,
                                         @RequestParam(value = "sort_by", defaultValue = "ID") ESortApartment sortBy,
                                         @RequestParam(value = "sort_direction", defaultValue = "DESC") Sort.Direction sortDirection) {
        IFindAllUserService.Input input = new IFindAllUserService.Input(page, size);
        input.createPageable(sortDirection, sortBy.getValue());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(userService.getFindAllUserService()
                        .execute(input)));
    }

    @ApiOperation(value = "Find Apartment Of User", authorizations = {@Authorization(value = "JWT")})
    @GetMapping(value = "/apartment/{id}")
    public ResponseEntity<?> findApartmentOfUser(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(userService.getFindDetailUserService()
                        .execute(id)));
    }

}
