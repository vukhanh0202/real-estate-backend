package com.uit.realestate.service.user.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.domain.action.Favourite;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.domain.user.User;
import com.uit.realestate.domain.user.UserTarget;
import com.uit.realestate.dto.apartment.ApartmentDto;
import com.uit.realestate.dto.response.FileCaption;
import com.uit.realestate.dto.response.PaginationResponse;
import com.uit.realestate.dto.user.UserDetailDto;
import com.uit.realestate.dto.user.UserDto;
import com.uit.realestate.dto.user.UserTargetDto;
import com.uit.realestate.exception.InvalidException;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.mapper.apartment.ApartmentMapper;
import com.uit.realestate.mapper.user.UserMapper;
import com.uit.realestate.payload.user.*;
import com.uit.realestate.repository.action.FavouriteRepository;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.repository.user.UserRepository;
import com.uit.realestate.repository.user.UserTargetRepository;
import com.uit.realestate.service.file.UploadService;
import com.uit.realestate.service.location.DistrictService;
import com.uit.realestate.service.user.UserService;
import com.uit.realestate.utils.MessageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private static final String AVATAR_FILE = "/avatar/";

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final MessageHelper messageHelper;

    private final ApartmentMapper apartmentMapper;
    private final ApartmentRepository apartmentRepository;
    private final FavouriteRepository favouriteRepository;
    private final UserTargetRepository userTargetRepository;
    private final UploadService uploadService;
    private final DistrictService districtService;

    @Override
    public PaginationResponse<UserDto> findAll(FindAllUserRequest req) {
        log.info("Find all User");
        Page<User> result = userRepository.findByUsernameContainingOrFullNameContainingOrEmailContaining(req.getSearch(),
                req.getSearch(), req.getSearch(), req.getPageable());
        return new PaginationResponse<>(
                result.getTotalElements()
                , result.getNumberOfElements()
                , result.getNumber() + 1
                , userMapper.toUserDtoList(result.getContent()));
    }

    @Override
    public UserDetailDto findDetail(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.User.NOT_FOUND)));
        log.info("Find detail user with ID: " + id);

        UserDetailDto result = userMapper.toUserDetailDto(user);
        result.setPostApartmentList(apartmentMapper.toApartmentPreviewDtoList(user.getApartments(), id, null));
        result.setFavouriteApartmentList(apartmentMapper
                .toApartmentPreviewDtoList(user.getFavourites()
                                .stream()
                                .map(Favourite::getApartment)
                                .collect(Collectors.toList()),
                        id, null));
        result.setTotalFavouriteApartment(result.getFavouriteApartmentList().size());
        result.setTotalPostApartment(result.getPostApartmentList().size());
        return result;
    }

    @Override
    public PaginationResponse<ApartmentDto> findByAuthor(FindUserApartmentAuthorRequest req) {
        if (userRepository.findById(req.getUserId()).isEmpty()) {
            throw new NotFoundException(messageHelper.getMessage(MessageCode.User.NOT_FOUND));
        }

        log.info("Find user apartment author from user ID: " + req.getUserId());
        Page<Apartment> result = apartmentRepository.findAllByAuthorIdAndStatusIn(req.getUserId(),
                List.of(EApartmentStatus.OPEN, EApartmentStatus.PENDING),
                req.getPageable());
        return new PaginationResponse<>(
                result.getTotalElements()
                , result.getNumberOfElements()
                , result.getNumber() + 1
                , apartmentMapper.toApartmentPreviewDtoList(result.getContent(), req.getUserId(), req.getIp()));
    }

    @Override
    public PaginationResponse<ApartmentDto> findByUserFavourite(FindUserApartmentFavouriteRequest req) {
        if (userRepository.findById(req.getUserId()).isEmpty()) {
            throw new NotFoundException(messageHelper.getMessage(MessageCode.User.NOT_FOUND));
        }

        log.info("Find user apartment favourite from user ID: " + req.getUserId());
        Page<Favourite> result = favouriteRepository.findAllByUserIdAndApartment_Status(req.getUserId(), EApartmentStatus.OPEN,
                req.getPageable());
        return new PaginationResponse(
                result.getTotalElements()
                , result.getNumberOfElements()
                , result.getNumber() + 1
                , apartmentMapper.toApartmentPreviewDtoList(result.getContent()
                .stream().map(Favourite::getApartment).collect(Collectors.toList()), req.getUserId(), req.getIp()));
    }

    @Override
    public UserDto findUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.User.NOT_FOUND)));

        log.info("Find information user from user ID");
        return userMapper.toUserDto(user);
    }

    @Override
    public List<UserTargetDto> findUserTarget(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.User.NOT_FOUND)));
        return userMapper.toUserTargetDtoList(user.getUserTargets());
    }

    @Override
    public boolean removeUserTarget(Long targetId) {
        UserTarget userTarget = userTargetRepository.findById(targetId)
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.UserTarget.NOT_FOUND)));
        log.info("Remove user target "+ targetId);
        userTargetRepository.delete(userTarget);
        return true;
    }

    @Override
    public Set<FileCaption> updateAvatar(UpdateAvatarUserRequest req) {
        var user = userRepository.findById(req.getId())
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.User.NOT_FOUND)));

        log.info("Save avatar : " + req.getId());
        Set<FileCaption> photos = uploadService.uploadPhoto(req.getFiles(), AVATAR_FILE,
                req.getId());
        req.setPhoto(photos.stream().findFirst().orElse(null));

        userMapper.updateAvatarUser(req, user);
        userRepository.save(user);
        return photos;
    }

    @Override
    public boolean updateInformation(UpdateUserRequest req) {
        User user = userRepository.findById(req.getId()).orElseThrow(() ->
                new NotFoundException(messageHelper.getMessage(MessageCode.User.NOT_FOUND)));
        districtService.validationDistrict(req.getAddress().getDistrictId(), req.getAddress().getProvinceId());

        log.info("Update information user from user ID");

        userMapper.updateUser(req, user);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean updateUserTarget(UpdateUserTargetRequest req) {
        districtService.validationDistrict(req.getDistrictId(), req.getProvinceId());

        log.info("Update user target ID:" + req.getId());

        UserTarget userTarget = userTargetRepository.findById(req.getId())
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.UserTarget.NOT_FOUND)));
        userMapper.updateUserTarget(req, userTarget);
        userTargetRepository.save(userTarget);
        return true;
    }

    @Override
    public boolean addUserTarget(AddUserTargetRequest req) {
        districtService.validationDistrict(req.getDistrictId(), req.getProvinceId());

        log.info("Add user target to user ID:" + req.getUserId());

        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.User.NOT_FOUND)));
        user.addTarget(userMapper.toUserTarget(req));
        userRepository.save(user);

        return true;
    }
}
