package com.uit.realestate.service.apartment.impl;

import com.uit.realestate.dto.apartment.ApartmentBasicDto;
import com.uit.realestate.mapper.apartment.ApartmentMapper;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.apartment.IFindHighlightApartmentService;
import com.uit.realestate.service.apartment.IFindLatestNewApartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FindHighlightApartmentServiceImpl extends AbstractBaseService<Void, List<ApartmentBasicDto>>
        implements IFindHighlightApartmentService<Void, List<ApartmentBasicDto>> {

    @Autowired
    ApartmentMapper apartmentMapper;

    @Autowired
    ApartmentRepository apartmentRepository;

    @Override
    public List<ApartmentBasicDto> doing(Void unused) {
        log.info("Find top 4 highlight apartment");
        return apartmentMapper.toApartmentBasicDtoList(apartmentRepository.findTop4ByHighlightTrueOrderByUpdatedAtDesc());
    }
}
