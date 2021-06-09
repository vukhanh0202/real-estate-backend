package com.uit.realestate.service.apartment.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.mapper.apartment.ApartmentMapper;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.apartment.IValidateApartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ValidateApartmentServiceImpl extends AbstractBaseService<IValidateApartmentService.Input, Boolean>
        implements IValidateApartmentService<IValidateApartmentService.Input, Boolean> {

    @Autowired
    ApartmentMapper apartmentMapper;

    @Autowired
    ApartmentRepository apartmentRepository;

    @Override
    public void preExecute(Input input) {
        Apartment apartment = apartmentRepository.findById(input.getId())
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.Apartment.NOT_FOUND)));
        if (!apartment.getStatus().equals(EApartmentStatus.PENDING)) {
            throw new NotFoundException(messageHelper.getMessage(MessageCode.Apartment.NOT_PENDING));
        }
    }

    @Override
    public Boolean doing(Input input) {
        log.info("Validate Apartment");
        Apartment apartment = apartmentRepository.findById(input.getId()).get();
        apartment.setStatus(input.getDecision() ? EApartmentStatus.OPEN : EApartmentStatus.CANCEL);
        return true;
    }

}
