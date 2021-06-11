package com.uit.realestate.controller;

import com.uit.realestate.data.UserPrincipal;
import com.uit.realestate.dto.response.ApiResponse;
import com.uit.realestate.payload.user.UpdateUserRequest;
import com.uit.realestate.service.location.ILocationService;
import com.uit.realestate.service.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
}
