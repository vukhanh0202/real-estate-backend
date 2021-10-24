package com.uit.realestate.controller;

import com.uit.realestate.constant.AppConstant;
import com.uit.realestate.constant.enums.sort.ESortApartment;
import com.uit.realestate.data.UserPrincipal;
import com.uit.realestate.dto.response.ApiResponse;
import com.uit.realestate.payload.user.UpdateAvatarUserRequest;
import com.uit.realestate.payload.user.UpdateUserRequest;
import com.uit.realestate.service.user.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
public class UserController {

    private final IFindUserByIdService findUserByIdService;

    private final IUpdateInformationByTokenService updateInformationByTokenService;

    private final IUpdateAvatarUserService updateAvatarUserService;

    private final IFindUserApartmentFavouriteService findUserApartmentFavouriteService;

    private final IFindUserApartmentAuthorService findUserApartmentAuthorService;

    @ApiOperation(value = "Find Info User By Token", authorizations = {@Authorization(value = "JWT")})
    @GetMapping(value = "/token")
    public ResponseEntity<?> findUserToken(){
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(findUserByIdService.execute(userPrincipal.getId())));
    }

    @ApiOperation(value = "Update information", authorizations = {@Authorization(value = "JWT")})
    @PutMapping(value = "/token/update")
    public ResponseEntity<?> updateInformationUser(@RequestBody UpdateUserRequest updateUserRequest){
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        updateUserRequest.setId(userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(updateInformationByTokenService.execute(updateUserRequest)));
    }

    @ApiOperation(value = "Update information", authorizations = {@Authorization(value = "JWT")})
    @PutMapping(value = "/token/update-avatar")
    public ResponseEntity<?> updateAvatarUser(@RequestParam("file") MultipartFile file){
        var userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UpdateAvatarUserRequest updateAvatarUserRequest = new UpdateAvatarUserRequest(userPrincipal.getId(), file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(updateAvatarUserService.execute(updateAvatarUserRequest)));
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
                .body(new ApiResponse(findUserApartmentFavouriteService.execute(input)));
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
                .body(new ApiResponse(findUserApartmentAuthorService.execute(input)));
    }
}
