package com.uit.realestate.service.apartment.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.apartment.ICloseApartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CloseApartmentServiceImpl extends AbstractBaseService<Long, Boolean>
        implements ICloseApartmentService {

    private final ApartmentRepository apartmentRepository;

    @Override
    public Boolean doing(Long id) {
        log.info("Close Apartment");
        Apartment apartment = apartmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.Apartment.NOT_FOUND)));
        apartment.setStatus(EApartmentStatus.CLOSE);
        apartmentRepository.save(apartment);
        return true;
    }

}
