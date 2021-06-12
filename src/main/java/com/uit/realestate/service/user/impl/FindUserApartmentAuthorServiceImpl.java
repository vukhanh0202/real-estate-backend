package com.uit.realestate.service.user.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.domain.user.User;
import com.uit.realestate.dto.apartment.ApartmentDto;
import com.uit.realestate.dto.response.PaginationResponse;
import com.uit.realestate.dto.user.UserDto;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.mapper.apartment.ApartmentMapper;
import com.uit.realestate.mapper.user.UserMapper;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.repository.user.UserRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.user.IFindUserApartmentAuthorService;
import com.uit.realestate.service.user.IFindUserByIdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FindUserApartmentAuthorServiceImpl extends AbstractBaseService<IFindUserApartmentAuthorService.Input, PaginationResponse<ApartmentDto>>
        implements IFindUserApartmentAuthorService<IFindUserApartmentAuthorService.Input, PaginationResponse<ApartmentDto>> {

    private final UserRepository userRepository;

    private final ApartmentMapper apartmentMapper;

    private final ApartmentRepository apartmentRepository;

    public FindUserApartmentAuthorServiceImpl(UserRepository userRepository, ApartmentMapper apartmentMapper, ApartmentRepository apartmentRepository) {
        this.userRepository = userRepository;
        this.apartmentMapper = apartmentMapper;
        this.apartmentRepository = apartmentRepository;
    }

    @Override
    public void preExecute(Input input) {
        if (userRepository.findById(input.getUserId()).isEmpty()) {
            throw new NotFoundException(messageHelper.getMessage(MessageCode.User.NOT_FOUND));
        }
    }

    @Override
    public PaginationResponse<ApartmentDto> doing(Input input) {
        log.info("Find user apartment author from user ID: " + input.getUserId());
        Page<Apartment> result = apartmentRepository.findAllByAuthorId(input.getUserId(), input.getPageable());
        return new PaginationResponse(
                result.getTotalElements()
                , result.getNumberOfElements()
                , result.getNumber() + 1
                , apartmentMapper.toApartmentPreviewDtoList(result.getContent(), input.getUserId()));
    }
}
