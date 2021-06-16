package com.uit.realestate.service.user.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.domain.action.Favourite;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.domain.user.User;
import com.uit.realestate.dto.apartment.ApartmentDto;
import com.uit.realestate.dto.response.PaginationResponse;
import com.uit.realestate.dto.user.UserDetailDto;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.mapper.apartment.ApartmentMapper;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.repository.user.UserRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.user.IFindDetailUserService;
import com.uit.realestate.service.user.IFindUserApartmentAuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Slf4j
public class FindDetailUserServiceImpl extends AbstractBaseService<Long, UserDetailDto>
        implements IFindDetailUserService<Long, UserDetailDto> {

    private final UserRepository userRepository;

    private final ApartmentMapper apartmentMapper;

    private final ApartmentRepository apartmentRepository;

    public FindDetailUserServiceImpl(UserRepository userRepository, ApartmentMapper apartmentMapper, ApartmentRepository apartmentRepository) {
        this.userRepository = userRepository;
        this.apartmentMapper = apartmentMapper;
        this.apartmentRepository = apartmentRepository;
    }

    @Override
    public void preExecute(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException(messageHelper.getMessage(MessageCode.User.NOT_FOUND));
        }
    }

    @Override
    public UserDetailDto doing(Long userId) {
        log.info("Find detail user with ID: " + userId);

        User user = userRepository.findById(userId).get();
        UserDetailDto result = new UserDetailDto();
        result.setPostApartmentList(apartmentMapper.toApartmentPreviewDtoList(user.getApartments(), userId));
        result.setFavouriteApartmentList(apartmentMapper
                .toApartmentPreviewDtoList(user.getFavourites()
                        .stream()
                        .map(Favourite::getApartment)
                        .collect(Collectors.toList()),
                        userId));
        result.setId(userId);
        result.setTotalFavouriteApartment(result.getFavouriteApartmentList().size());
        result.setTotalPostApartment(result.getPostApartmentList().size());
        result.setTotalRecommendApartment(apartmentRepository.findRecommendApartmentByUserId(userId).size());
        return result;
    }
}
