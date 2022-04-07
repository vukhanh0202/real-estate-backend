package com.uit.realestate.service.user;

import com.uit.realestate.dto.apartment.ApartmentDto;
import com.uit.realestate.dto.response.FileCaption;
import com.uit.realestate.dto.response.PaginationResponse;
import com.uit.realestate.dto.user.UserDetailDto;
import com.uit.realestate.dto.user.UserDto;
import com.uit.realestate.dto.user.UserTargetDto;
import com.uit.realestate.payload.user.*;

import java.util.List;
import java.util.Set;

public interface UserService {

    PaginationResponse<UserDto> findAll(FindAllUserRequest req);

    UserDetailDto findDetail(Long id);

    PaginationResponse<ApartmentDto> findByAuthor(FindUserApartmentAuthorRequest req);

    PaginationResponse<ApartmentDto> findByUserFavourite(FindUserApartmentFavouriteRequest req);

    UserDto findUserById(Long userId);

    List<UserTargetDto> findUserTarget(Long userId);

    boolean removeUserTarget(Long targetId);

    Set<FileCaption> updateAvatar(UpdateAvatarUserRequest req);

    boolean updateInformation(UpdateUserRequest req);

    boolean updateUserTarget(UpdateUserTargetRequest req);

    boolean addUserTarget(AddUserTargetRequest req);
}
