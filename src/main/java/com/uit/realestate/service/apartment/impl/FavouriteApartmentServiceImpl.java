package com.uit.realestate.service.apartment.impl;

import com.uit.realestate.constant.AppConstant;
import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.domain.action.Favourite;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.domain.user.User;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.mapper.apartment.ApartmentMapper;
import com.uit.realestate.repository.action.FavouriteRepository;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.repository.user.UserRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.apartment.IFavouriteApartmentService;
import com.uit.realestate.service.apartment.IValidateApartmentService;
import com.uit.realestate.service.tracking.TrackingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FavouriteApartmentServiceImpl extends AbstractBaseService<IFavouriteApartmentService.Input, Boolean>
        implements IFavouriteApartmentService<IFavouriteApartmentService.Input, Boolean> {

    private final ApartmentMapper apartmentMapper;

    private final ApartmentRepository apartmentRepository;

    private final UserRepository userRepository;

    private final FavouriteRepository favouriteRepository;

    private final TrackingService tracking;

    public FavouriteApartmentServiceImpl(ApartmentMapper apartmentMapper, ApartmentRepository apartmentRepository, UserRepository userRepository, FavouriteRepository favouriteRepository, TrackingService tracking) {
        this.apartmentMapper = apartmentMapper;
        this.apartmentRepository = apartmentRepository;
        this.userRepository = userRepository;
        this.favouriteRepository = favouriteRepository;
        this.tracking = tracking;
    }

    @Override
    public Boolean doing(Input input) {
        Apartment apartment = apartmentRepository.findById(input.getApartmentId())
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.Apartment.NOT_FOUND)));
        User user = userRepository.findById(input.getUserId())
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.User.NOT_FOUND)));
        Favourite favourite = favouriteRepository.findByApartmentIdAndUserId(input.getApartmentId(), input.getUserId())
                .orElse(null);
        if (favourite == null) {
            log.info("Favourite Apartment with apartment ID: " + input.getApartmentId());

            user.addFavourite(new Favourite(apartment, user));
            tracking.trackingCategory(input.getUserId(), input.getIp(), apartment.getCategory().getId(), AppConstant.DEFAULT_FAVOURITE_RATING);
            tracking.trackingDistrict(input.getUserId(), input.getIp(), apartment.getApartmentAddress().getDistrict().getId(), AppConstant.DEFAULT_FAVOURITE_RATING);
            tracking.trackingProvince(input.getUserId(), input.getIp(), apartment.getApartmentAddress().getProvince().getId(), AppConstant.DEFAULT_FAVOURITE_RATING);
        } else {
            log.info("Disable favourite Apartment  with apartment ID: " + input.getApartmentId());
            user.removeFavourite(favourite);
            tracking.trackingCategory(input.getUserId(), input.getIp(), apartment.getCategory().getId(), AppConstant.DEFAULT_DISABLE_FAVOURITE_RATING);
            tracking.trackingDistrict(input.getUserId(), input.getIp(), apartment.getApartmentAddress().getDistrict().getId(), AppConstant.DEFAULT_DISABLE_FAVOURITE_RATING);
            tracking.trackingProvince(input.getUserId(), input.getIp(), apartment.getApartmentAddress().getProvince().getId(), AppConstant.DEFAULT_DISABLE_FAVOURITE_RATING);
        }
        userRepository.save(user);
        return true;
    }

}
