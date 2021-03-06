package com.uit.realestate.controller.dashboard;

import com.uit.realestate.constant.AppConstant;
import com.uit.realestate.constant.enums.sort.ESortUser;
import com.uit.realestate.dto.response.ApiResponse;
import com.uit.realestate.payload.user.FindAllUserRequest;
import com.uit.realestate.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Api(value = "Category Dashboard APIs")
@RequestMapping("/dashboard/user")
@RequiredArgsConstructor
public class DashboardUserController {

    private final UserService userService;

    @ApiOperation(value = "Find All User", authorizations = {@Authorization(value = "JWT")})
    @GetMapping(value = "/search")
    @PreAuthorize("@securityService.hasRoles('ADMIN')")
    public ResponseEntity<?> findAllUser(@RequestParam(value = "page", defaultValue = AppConstant.PAGE_NUMBER_DEFAULT) Integer page,
                                         @RequestParam(value = "size", defaultValue = AppConstant.PAGE_SIZE_DEFAULT) Integer size,
                                         @RequestParam(value = "sort_by", defaultValue = "ID") ESortUser sortBy,
                                         @RequestParam(value = "sort_direction", defaultValue = "DESC") Sort.Direction sortDirection,
                                         @RequestParam(value = "search", defaultValue = "") String search
                ) {
        FindAllUserRequest req = new FindAllUserRequest(page, size, search);
        req.createPageable(sortDirection, sortBy.getValue());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(userService.findAll(req)));
    }

    @ApiOperation(value = "Find Apartment Of User", authorizations = {@Authorization(value = "JWT")})
    @GetMapping(value = "/apartment/{id}")
    @PreAuthorize("@securityService.hasRoles('ADMIN')")
    public ResponseEntity<?> findApartmentOfUser(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(userService.findDetail(id)));
    }

}
