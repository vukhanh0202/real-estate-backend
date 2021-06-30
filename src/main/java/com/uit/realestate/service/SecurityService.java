package com.uit.realestate.service;

import com.uit.realestate.data.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class SecurityService {

    public boolean hasRoles(String... roles) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Arrays.asList(roles).contains(userPrincipal.getRoleCode().name());
    }
}
