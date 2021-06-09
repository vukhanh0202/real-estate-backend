package com.uit.realestate.service.service.impl;

import com.uit.realestate.service.service.IFindUserByIdService;
import com.uit.realestate.service.service.IUpdateInformationByTokenService;
import com.uit.realestate.service.service.IUserService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
@Slf4j
public class UserServiceImpl implements IUserService {


    @Autowired
    private IFindUserByIdService findUserByIdService;

    @Autowired
    private IUpdateInformationByTokenService updateInformationByTokenService;
}
