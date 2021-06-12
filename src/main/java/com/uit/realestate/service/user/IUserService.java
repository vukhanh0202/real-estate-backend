package com.uit.realestate.service.user;

import com.uit.realestate.dto.apartment.ApartmentDto;
import com.uit.realestate.dto.response.PaginationResponse;
import com.uit.realestate.dto.user.UserDto;
import com.uit.realestate.payload.user.UpdateUserRequest;


public interface IUserService {

    IFindUserByIdService<Long, UserDto> getFindUserByIdService();

    IUpdateInformationByTokenService<UpdateUserRequest, Boolean> getUpdateInformationByTokenService();

    IFindUserApartmentAuthorService<IFindUserApartmentAuthorService.Input, PaginationResponse<ApartmentDto>> getFindUserApartmentAuthorService();

    IFindUserApartmentFavouriteService<IFindUserApartmentFavouriteService.Input, PaginationResponse<ApartmentDto>> getFindUserApartmentFavouriteService();
}
