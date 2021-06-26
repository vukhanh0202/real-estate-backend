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

    private final IFindAllUserService findAllUserService;

    private final IFindDetailUserService findDetailUserService;

    private final IUpdateAvatarUserService updateAvatarUserService;

    public UserServiceImpl(IFindUserByIdService findUserByIdService, IUpdateInformationByTokenService updateInformationByTokenService, IFindUserApartmentAuthorService findUserApartmentAuthorService, IFindUserApartmentFavouriteService findUserApartmentFavouriteService, IFindAllUserService findAllUserService, IFindDetailUserService findDetailUserService, IUpdateAvatarUserService updateAvatarUserService) {
        this.findUserByIdService = findUserByIdService;
        this.updateInformationByTokenService = updateInformationByTokenService;
        this.findUserApartmentAuthorService = findUserApartmentAuthorService;
        this.findUserApartmentFavouriteService = findUserApartmentFavouriteService;
        this.findAllUserService = findAllUserService;
        this.findDetailUserService = findDetailUserService;
        this.updateAvatarUserService = updateAvatarUserService;
    }
}
