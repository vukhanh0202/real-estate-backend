package com.uit.realestate.controller;

import com.uit.realestate.configuration.config.JwtTokenUtil;
import com.uit.realestate.data.UserPrincipal;
import com.uit.realestate.dto.auth.UserLoginDto;
import com.uit.realestate.dto.response.ApiResponse;
import com.uit.realestate.exception.ForbiddenException;
import com.uit.realestate.payload.auth.ChangePasswordRequest;
import com.uit.realestate.payload.auth.NewAccountRequest;
import com.uit.realestate.service.auth.AuthService;
import com.uit.realestate.service.auth.JwtUserDetailsService;
import com.uit.realestate.utils.MessageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static com.uit.realestate.constant.MessageCode.User.USER_WRONG;

@RestController
@Slf4j
@RequestMapping("/auth")
@Api(value = "Auth APIs")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    private final JwtUserDetailsService userDetailsService;

    private final MessageHelper messageHelper;

    private final AuthService authService;

    @ApiOperation(value = "Login")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserLoginDto authenticationRequest){

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        JSONObject json = new JSONObject();
        json.put("access_token", token);
        log.info("Auth Controller: put access token");
        return ResponseEntity.status(HttpStatus.OK).body(json.toString());
    }

    private void authenticate(String username, String password){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException | BadCredentialsException e) {
            throw new ForbiddenException(messageHelper.getMessage(USER_WRONG,username));
        }
    }

    @ApiOperation(value = "Register")
    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@RequestBody NewAccountRequest newAccountRequest){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(authService.register(newAccountRequest)));
    }

    @ApiOperation(value = "Change Password")
    @PutMapping(value = "/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest){
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        changePasswordRequest.setId(userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(authService.changePassword(changePasswordRequest)));
    }

}
