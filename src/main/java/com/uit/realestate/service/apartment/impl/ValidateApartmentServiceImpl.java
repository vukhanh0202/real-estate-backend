package com.uit.realestate.service.apartment.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.apartment.IValidateApartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ValidateApartmentServiceImpl extends AbstractBaseService<IValidateApartmentService.Input, Boolean>
        implements IValidateApartmentService {

    private final ApartmentRepository apartmentRepository;

    @Override
    public Boolean doing(Input input) {
        log.info("Validate Apartment");
        var apartment = apartmentRepository.findById(input.getId())
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.Apartment.NOT_FOUND)));
        apartment.setStatus(input.getDecision() ? EApartmentStatus.OPEN : EApartmentStatus.CLOSE);
        return true;
    }
}
