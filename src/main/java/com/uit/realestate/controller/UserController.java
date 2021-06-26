package com.uit.realestate.controller;

import com.uit.realestate.constant.AppConstant;
import com.uit.realestate.constant.enums.sort.ESortApartment;
import com.uit.realestate.data.UserPrincipal;
import com.uit.realestate.dto.response.ApiResponse;
import com.uit.realestate.payload.user.UpdateAvatarUserRequest;
import com.uit.realestate.payload.user.UpdateUserRequest;
import com.uit.realestate.service.apartment.ISearchApartmentService;
import com.uit.realestate.service.user.IFindUserApartmentAuthorService;
import com.uit.realestate.service.user.IFindUserApartmentFavouriteService;
import com.uit.realestate.service.user.IUserService;
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
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping("/user")
@Api(value = "User APIs")
public class UserController {

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "Find Info User By Token", authorizations = {@Authorization(value = "JWT")})
    @GetMapping(value = "/token")
    public ResponseEntity<?> findUserToken(){
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(userService.getFindUserByIdService()
                        .execute(userPrincipal.getId())));
    }

    @ApiOperation(value = "Update information", authorizations = {@Authorization(value = "JWT")})
    @PutMapping(value = "/token/update")
    public ResponseEntity<?> updateInformationUser(@RequestBody UpdateUserRequest updateUserRequest){
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        updateUserRequest.setId(userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(userService.getUpdateInformationByTokenService()
                        .execute(updateUserRequest)));
    }

    @ApiOperation(value = "Update information", authorizations = {@Authorization(value = "JWT")})
    @PutMapping(value = "/token/update-avatar")
    public ResponseEntity<?> updateAvatarUser(@RequestParam("file") MultipartFile file){
        var userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UpdateAvatarUserRequest updateAvatarUserRequest = new UpdateAvatarUserRequest(userPrincipal.getId(), file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(userService.getUpdateAvatarUserService()
                        .execute(updateAvatarUserRequest)));
    }

    @ApiOperation(value = "Get apartment favourite", authorizations = {@Authorization(value = "JWT")})
    @GetMapping(value = "/token/apartment/favourite")
    public ResponseEntity<?> getApartmentFavourite(@RequestParam(value = "page", defaultValue = AppConstant.PAGE_NUMBER_DEFAULT) Integer page,
                                                   @RequestParam(value = "size", defaultValue = AppConstant.PAGE_SIZE_DEFAULT) Integer size,
                                                   @RequestParam(value = "sort_by", defaultValue = "ID") ESortApartment sortBy,
                                                   @RequestParam(value = "sort_direction", defaultValue = "DESC") Sort.Direction sortDirection){
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        IFindUserApartmentFavouriteService.Input input = new IFindUserApartmentFavouriteService.Input(page, size, userPrincipal.getId());
        input.createPageable(sortDirection, sortBy.getValue());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(userService.getFindUserApartmentFavouriteService()
                        .execute(input)));
    }

    @ApiOperation(value = "Get apartment author", authorizations = {@Authorization(value = "JWT")})
    @GetMapping(value = "/token/apartment/author")
    public ResponseEntity<?> getApartmentAuthor(@RequestParam(value = "page", defaultValue = AppConstant.PAGE_NUMBER_DEFAULT) Integer page,
                                                @RequestParam(value = "size", defaultValue = AppConstant.PAGE_SIZE_DEFAULT) Integer size,
                                                @RequestParam(value = "sort_by", defaultValue = "ID") ESortApartment sortBy,
                                                @RequestParam(value = "sort_direction", defaultValue = "DESC") Sort.Direction sortDirection){
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        IFindUserApartmentAuthorService.Input input = new IFindUserApartmentAuthorService.Input(page, size, userPrincipal.getId());
        input.createPageable(sortDirection, sortBy.getValue());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(userService.getFindUserApartmentAuthorService()
                        .execute(input)));
    }
}
