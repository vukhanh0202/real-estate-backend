package com.uit.realestate.service.apartment.impl;

import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.dto.apartment.ApartmentBasicDto;
import com.uit.realestate.mapper.apartment.ApartmentMapper;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.apartment.IFindHighlightApartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FindHighlightApartmentServiceImpl extends AbstractBaseService<Long, List<ApartmentBasicDto>>
        implements IFindHighlightApartmentService<Long, List<ApartmentBasicDto>> {

    private final ApartmentMapper apartmentMapper;

    private final ApartmentRepository apartmentRepository;

    public FindHighlightApartmentServiceImpl(ApartmentMapper apartmentMapper, ApartmentRepository apartmentRepository) {
        this.apartmentMapper = apartmentMapper;
        this.apartmentRepository = apartmentRepository;
    }

    @Override
    public List<ApartmentBasicDto> doing(Long userId) {
        log.info("Find top 4 highlight apartment");
        return apartmentMapper.toApartmentBasicDtoList(apartmentRepository
                .findTop4ByHighlightTrueAndStatusOrderByUpdatedAtDesc(EApartmentStatus.OPEN), userId);
    }
}
