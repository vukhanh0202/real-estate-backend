package com.uit.realestate.service.user.impl;

import com.uit.realestate.service.user.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
@Slf4j
public class UserServiceImpl implements IUserService {


    private final IFindUserByIdService findUserByIdService;

    private final IUpdateInformationByTokenService updateInformationByTokenService;

    private final IFindUserApartmentAuthorService findUserApartmentAuthorService;

    private final IFindUserApartmentFavouriteService findUserApartmentFavouriteService;

    public UserServiceImpl(IFindUserByIdService findUserByIdService, IUpdateInformationByTokenService updateInformationByTokenService, IFindUserApartmentAuthorService findUserApartmentAuthorService, IFindUserApartmentFavouriteService findUserApartmentFavouriteService) {
        this.findUserByIdService = findUserByIdService;
        this.updateInformationByTokenService = updateInformationByTokenService;
        this.findUserApartmentAuthorService = findUserApartmentAuthorService;
        this.findUserApartmentFavouriteService = findUserApartmentFavouriteService;
    }
}
