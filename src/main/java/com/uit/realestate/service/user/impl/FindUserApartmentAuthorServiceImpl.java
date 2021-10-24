package com.uit.realestate.service.user.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.dto.apartment.ApartmentDto;
import com.uit.realestate.dto.response.PaginationResponse;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.mapper.apartment.ApartmentMapper;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.repository.user.UserRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.user.IFindUserApartmentAuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FindUserApartmentAuthorServiceImpl extends AbstractBaseService<IFindUserApartmentAuthorService.Input, PaginationResponse<ApartmentDto>>
        implements IFindUserApartmentAuthorService {

    private final UserRepository userRepository;

    private final ApartmentMapper apartmentMapper;

    private final ApartmentRepository apartmentRepository;

    @Override
    public void preExecute(Input input) {
        if (userRepository.findById(input.getUserId()).isEmpty()) {
            throw new NotFoundException(messageHelper.getMessage(MessageCode.User.NOT_FOUND));
        }
    }

    @Override
    public PaginationResponse<ApartmentDto> doing(Input input) {
        log.info("Find user apartment author from user ID: " + input.getUserId());
        Page<Apartment> result = apartmentRepository.findAllByAuthorIdAndStatusIn(input.getUserId(),
                List.of(EApartmentStatus.OPEN, EApartmentStatus.PENDING),
                input.getPageable());
        return new PaginationResponse(
                result.getTotalElements()
                , result.getNumberOfElements()
                , result.getNumber() + 1
                , apartmentMapper.toApartmentPreviewDtoList(result.getContent(), input.getUserId()));
    }
}
