package com.uit.realestate.service.apartment.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.exception.InvalidException;
import com.uit.realestate.mapper.apartment.ApartmentMapper;
import com.uit.realestate.payload.apartment.AddApartmentRequest;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.repository.location.DistrictRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.apartment.IAddApartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddApartmentServiceImpl extends AbstractBaseService<AddApartmentRequest, Boolean>
        implements IAddApartmentService {

    private final ApartmentMapper apartmentMapper;

    private final ApartmentRepository apartmentRepository;

    private final DistrictRepository districtRepository;

    @Override
    public void preExecute(AddApartmentRequest addApartmentRequest) {
        if (addApartmentRequest.getApartmentAddress() != null){
            Long districtId = addApartmentRequest.getApartmentAddress().getDistrictId();
            Long provinceId = addApartmentRequest.getApartmentAddress().getProvinceId();
            if (!districtRepository.findById(districtId).get().getProvince().getId().equals(provinceId)){
                throw new InvalidException(messageHelper.getMessage(MessageCode.Address.INVALID));
            }
        }
    }

    @Override
    public Boolean doing(AddApartmentRequest addApartmentRequest) {
        log.info("Add a new Apartment");
        apartmentRepository.save(apartmentMapper.toApartment(addApartmentRequest));
        return true;
    }
}
