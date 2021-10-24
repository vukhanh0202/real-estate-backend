package com.uit.realestate.service.apartment.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.apartment.IHighlightApartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class HighlightApartmentServiceImpl extends AbstractBaseService<Long, Boolean>
        implements IHighlightApartmentService {

    private final ApartmentRepository apartmentRepository;

    @Override
    public Boolean doing(Long id) {
        Apartment apartment = apartmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.Apartment.NOT_FOUND)));
        if(apartment.getHighlight()){
            log.info("UnHighlight apartment with ID:" + id);
            apartment.setHighlight(false);
        }else{
            log.info("Highlight apartment with ID:" + id);
            apartment.setHighlight(true);
        }
        apartmentRepository.save(apartment);
        return true;
    }
}
