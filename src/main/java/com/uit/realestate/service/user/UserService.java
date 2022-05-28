package com.uit.realestate.service.user;

import com.uit.realestate.dto.apartment.ApartmentDto;
import com.uit.realestate.dto.response.FileCaption;
import com.uit.realestate.dto.response.PaginationResponse;
import com.uit.realestate.dto.user.UserDetailDto;
import com.uit.realestate.dto.user.UserDto;
import com.uit.realestate.payload.user.*;

import java.util.Set;

public interface UserService {

    PaginationResponse<UserDto> findAll(FindAllUserRequest req);

    UserDetailDto findDetail(Long id);

    PaginationResponse<ApartmentDto> findByAuthor(FindUserApartmentAuthorRequest req);

    PaginationResponse<ApartmentDto> findByUserFavourite(FindUserApartmentFavouriteRequest req);

    UserDto findUserById(Long userId);

    Set<FileCaption> updateAvatar(UpdateAvatarUserRequest req);

    boolean updateInformation(UpdateUserRequest req);

}
