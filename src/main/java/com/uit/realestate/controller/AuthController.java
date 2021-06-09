package com.uit.realestate.controller;

import com.uit.realestate.configuration.JwtTokenUtil;
import com.uit.realestate.dto.auth.UserLoginDto;
import com.uit.realestate.exception.ForbiddenException;
import com.uit.realestate.service.auth.JwtUserDetailsService;
import com.uit.realestate.utils.MessageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static com.uit.realestate.constant.MessageCode.User.USER_WRONG;

@RestController
@Slf4j
@RequestMapping("/auth")
@Api(value = "Auth APIs")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    private final JwtUserDetailsService userDetailsService;

    private final MessageHelper messageHelper;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, JwtUserDetailsService userDetailsService, MessageHelper messageHelper) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.messageHelper = messageHelper;
    }

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
}
