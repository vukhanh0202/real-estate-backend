package com.uit.realestate.service.apartment.impl;

import com.uit.realestate.dto.apartment.ApartmentBasicDto;
import com.uit.realestate.dto.apartment.ApartmentDto;
import com.uit.realestate.mapper.apartment.ApartmentMapper;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.apartment.IFindLatestNewApartmentService;
import com.uit.realestate.service.apartment.ISearchApartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FindLatestNewApartmentServiceImpl extends AbstractBaseService<Void, List<ApartmentBasicDto>>
        implements IFindLatestNewApartmentService<Void, List<ApartmentBasicDto>> {

    @Autowired
    ApartmentMapper apartmentMapper;

    @Autowired
    ApartmentRepository apartmentRepository;

    @Override
    public List<ApartmentBasicDto> doing(Void unused) {
        log.info("Find top 4 new latest apartment");
        return apartmentMapper.toApartmentBasicDtoList(apartmentRepository.findTop4ByOrderByCreatedAtDesc());
    }
}
