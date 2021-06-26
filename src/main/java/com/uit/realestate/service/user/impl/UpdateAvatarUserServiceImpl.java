package com.uit.realestate.service.user.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.domain.user.User;
import com.uit.realestate.dto.apartment.ApartmentDto;
import com.uit.realestate.dto.response.ApiResponse;
import com.uit.realestate.dto.response.FileCaption;
import com.uit.realestate.dto.response.PaginationResponse;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.mapper.apartment.ApartmentMapper;
import com.uit.realestate.mapper.user.UserMapper;
import com.uit.realestate.payload.user.UpdateAvatarUserRequest;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.repository.user.UserRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.file.UploadService;
import com.uit.realestate.service.user.IFindUserApartmentAuthorService;
import com.uit.realestate.service.user.IUpdateAvatarUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UpdateAvatarUserServiceImpl extends AbstractBaseService<UpdateAvatarUserRequest, Set<FileCaption>>
        implements IUpdateAvatarUserService<UpdateAvatarUserRequest, Set<FileCaption>> {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private static final String AVATAR_FILE = "/avatar/";

    @Autowired
    private UploadService uploadService;

    public UpdateAvatarUserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public void preExecute(UpdateAvatarUserRequest updateAvatarUserRequest) {
        if (userRepository.findById(updateAvatarUserRequest.getId()).isEmpty()) {
            throw new NotFoundException(messageHelper.getMessage(MessageCode.User.NOT_FOUND));
        }
    }

    @Override
    public Set<FileCaption> doing(UpdateAvatarUserRequest updateAvatarUserRequest) {
        log.info("Save avatar : " + updateAvatarUserRequest.getId());
        Set<FileCaption> photos = uploadService.uploadPhoto(updateAvatarUserRequest.getFiles(), AVATAR_FILE,
                updateAvatarUserRequest.getId());
        updateAvatarUserRequest.setPhoto(photos.stream().findFirst().orElse(null));
        User user = userRepository.findById(updateAvatarUserRequest.getId()).get();
        userMapper.updateAvatarUser(updateAvatarUserRequest, user);
        userRepository.save(user);
        return photos;
    }
}
