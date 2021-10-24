package com.uit.realestate.service.apartment.impl;

import com.uit.realestate.constant.AppConstant;
import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.constant.enums.user.ERoleType;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.domain.user.User;
import com.uit.realestate.dto.apartment.ApartmentDto;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.mapper.apartment.ApartmentMapper;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.repository.user.UserRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.apartment.IGetApartmentDetailService;
import com.uit.realestate.service.tracking.TrackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetApartmentDetailServiceImpl extends AbstractBaseService<IGetApartmentDetailService.Input, ApartmentDto>
        implements IGetApartmentDetailService {

    private final TrackingService tracking;

    private final ApartmentMapper apartmentMapper;

    private final ApartmentRepository apartmentRepository;

    private final UserRepository userRepository;

    @Override
    public void preExecute(Input input) {
        Apartment apartment = apartmentRepository.findById(input.getId()).orElse(null);
        if (apartment == null) {
            throw new NotFoundException(messageHelper.getMessage(MessageCode.Apartment.NOT_FOUND));
        }
        if (apartment.getStatus() != EApartmentStatus.OPEN) {
            if (input.getUserId() == null) {
                throw new NotFoundException(messageHelper.getMessage(MessageCode.Apartment.NOT_FOUND));
            } else {
                User user = userRepository.findById(input.getUserId()).orElseThrow(() ->
                        new NotFoundException(messageHelper.getMessage(MessageCode.User.NOT_FOUND)));
                if (apartment.getAuthor() != user && !user.getRole().getId().equals(ERoleType.ADMIN)) {
                    throw new NotFoundException(messageHelper.getMessage(MessageCode.Apartment.NOT_FOUND));
                }
            }
        }
    }

    @Override
    public ApartmentDto doing(Input input) {
        Apartment apartment = apartmentRepository.findById(input.getId()).get();
        log.info("Tracking User");
        tracking.trackingCategory(input.getUserId(), input.getIp(), apartment.getCategory().getId(), AppConstant.DEFAULT_RATING);
        tracking.trackingDistrict(input.getUserId(), input.getIp(), apartment.getApartmentAddress().getDistrict().getId(), AppConstant.DEFAULT_RATING);
        tracking.trackingProvince(input.getUserId(), input.getIp(), apartment.getApartmentAddress().getProvince().getId(), AppConstant.DEFAULT_RATING);

        log.info("Get detail apartment ID: " + input.getId());

        return apartmentMapper.toApartmentFullDto(apartment, input.getUserId());
    }
}
