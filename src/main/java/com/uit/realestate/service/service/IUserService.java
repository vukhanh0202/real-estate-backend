package com.uit.realestate.service.service;

import com.uit.realestate.dto.user.UserDto;
import com.uit.realestate.payload.user.UpdateUserRequest;


public interface IUserService {

    IFindUserByIdService<Long, UserDto> getFindUserByIdService();

    IUpdateInformationByTokenService<UpdateUserRequest, Boolean> getUpdateInformationByTokenService();
}
