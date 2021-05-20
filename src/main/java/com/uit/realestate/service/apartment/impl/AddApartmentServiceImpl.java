package com.uit.realestate.service.apartment.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.dto.apartment.ApartmentDto;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.mapper.apartment.ApartmentMapper;
import com.uit.realestate.payload.apartment.AddApartmentRequest;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.apartment.IAddApartmentService;
import com.uit.realestate.service.apartment.IGetApartmentDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AddApartmentServiceImpl extends AbstractBaseService<AddApartmentRequest, Boolean>
        implements IAddApartmentService<AddApartmentRequest, Boolean> {

    @Autowired
    ApartmentMapper apartmentMapper;

    @Autowired
    ApartmentRepository apartmentRepository;


    @Override
    public Boolean doing(AddApartmentRequest addApartmentRequest) {
        log.info("Add a new Apartment");
        apartmentRepository.save(apartmentMapper.toApartment(addApartmentRequest));
        return true;
    }
}
