package com.uit.realestate.controller;

import com.uit.realestate.constant.AppConstant;
import com.uit.realestate.constant.enums.sort.ESortApartment;
import com.uit.realestate.data.UserPrincipal;
import com.uit.realestate.dto.response.ApiResponse;
import com.uit.realestate.payload.user.*;
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

    private final UserService userService;

    @ApiOperation(value = "Find Info User By Token", authorizations = {@Authorization(value = "JWT")})
    @GetMapping(value = "/token")
    public ResponseEntity<?> findUserToken(){
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(userService.findUserById(userPrincipal.getId())));
    }

    @ApiOperation(value = "Update information", authorizations = {@Authorization(value = "JWT")})
    @PutMapping(value = "/token/update")
    public ResponseEntity<?> updateInformationUser(@RequestBody UpdateUserRequest updateUserRequest){
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        updateUserRequest.setId(userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(userService.updateInformation(updateUserRequest)));
    }

    @ApiOperation(value = "Update information", authorizations = {@Authorization(value = "JWT")})
    @PutMapping(value = "/token/update-avatar")
    public ResponseEntity<?> updateAvatarUser(@RequestParam("file") MultipartFile file){
        var userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UpdateAvatarUserRequest updateAvatarUserRequest = new UpdateAvatarUserRequest(userPrincipal.getId(), file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(userService.updateAvatar(updateAvatarUserRequest)));
    }

    @ApiOperation(value = "Get apartment favourite", authorizations = {@Authorization(value = "JWT")})
    @GetMapping(value = "/token/apartment/favourite")
    public ResponseEntity<?> getApartmentFavourite(@RequestParam(value = "page", defaultValue = AppConstant.PAGE_NUMBER_DEFAULT) Integer page,
                                                   @RequestParam(value = "size", defaultValue = AppConstant.PAGE_SIZE_DEFAULT) Integer size,
                                                   @RequestParam(value = "sort_by", defaultValue = "ID") ESortApartment sortBy,
                                                   @RequestParam(value = "sort_direction", defaultValue = "DESC") Sort.Direction sortDirection){
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        FindUserApartmentFavouriteRequest req = new FindUserApartmentFavouriteRequest(page, size, userPrincipal.getId());
        req.createPageable(sortDirection, sortBy.getValue());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(userService.findByUserFavourite(req)));
    }

    @ApiOperation(value = "Get apartment author", authorizations = {@Authorization(value = "JWT")})
    @GetMapping(value = "/token/apartment/author")
    public ResponseEntity<?> getApartmentAuthor(@RequestParam(value = "page", defaultValue = AppConstant.PAGE_NUMBER_DEFAULT) Integer page,
                                                @RequestParam(value = "size", defaultValue = AppConstant.PAGE_SIZE_DEFAULT) Integer size,
                                                @RequestParam(value = "sort_by", defaultValue = "ID") ESortApartment sortBy,
                                                @RequestParam(value = "sort_direction", defaultValue = "DESC") Sort.Direction sortDirection){
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        FindUserApartmentAuthorRequest req = new FindUserApartmentAuthorRequest(page, size, userPrincipal.getId());
        req.createPageable(sortDirection, sortBy.getValue());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(userService.findByAuthor(req)));
    }

    @ApiOperation(value = "Add user target", authorizations = {@Authorization(value = "JWT")})
    @PostMapping(value = "/token/target")
    public ResponseEntity<?> addUserTarget(@RequestBody AddUserTargetRequest req){
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        req.setUserId(userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(userService.addUserTarget(req)));
    }

    @ApiOperation(value = "Update user target", authorizations = {@Authorization(value = "JWT")})
    @PutMapping(value = "/token/target")
    public ResponseEntity<?> updateUserTarget(@RequestBody UpdateUserTargetRequest req){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(userService.updateUserTarget(req)));
    }

    @ApiOperation(value = "Find user target", authorizations = {@Authorization(value = "JWT")})
    @GetMapping(value = "/token/target")
    public ResponseEntity<?> findUserTarget(){
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(userService.findUserTarget(userPrincipal.getId())));
    }

    @ApiOperation(value = "Remove user target", authorizations = {@Authorization(value = "JWT")})
    @DeleteMapping(value = "/token/target")
    public ResponseEntity<?> removeUserTarget(@RequestParam(value = "id") Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(userService.removeUserTarget(id)));
    }
}
