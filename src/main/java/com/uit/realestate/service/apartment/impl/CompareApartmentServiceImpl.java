package com.uit.realestate.service.apartment.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.dto.apartment.ApartmentCompareDto;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.mapper.apartment.ApartmentMapper;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.apartment.ICompareApartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompareApartmentServiceImpl extends AbstractBaseService<ICompareApartmentService.Input, List<ApartmentCompareDto>>
        implements ICompareApartmentService {

    private final ApartmentMapper apartmentMapper;

    private final ApartmentRepository apartmentRepository;

    @Override
    public void preExecute(ICompareApartmentService.Input input) {
        input.getIds().forEach(item -> {
            if (!apartmentRepository.existsById(item)) {
                throw new NotFoundException(messageHelper.getMessage(MessageCode.Apartment.NOT_FOUND));
            }
        });
    }

    @Override
    public List<ApartmentCompareDto> doing(ICompareApartmentService.Input input) {
        log.info("Compare Apartment");
        return apartmentMapper.toApartmentCompareDtoList(apartmentRepository.findAllByIdIn(input.getIds()), input.getUserId());
    }
}
