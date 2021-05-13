package com.uit.realestate.service.apartment.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.dto.apartment.ApartmentDto;
import com.uit.realestate.dto.category.CategoryDto;
import com.uit.realestate.exception.BadRequestException;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.mapper.apartment.ApartmentMapper;
import com.uit.realestate.mapper.category.CategoryMapper;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.repository.category.CategoryRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.apartment.IGetApartmentDetailService;
import com.uit.realestate.service.category.IFindAllCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.uit.realestate.constant.MessageCode.Token.INVALID_TOKEN;

@Service
@Slf4j
public class GetApartmentDetailServiceImpl extends AbstractBaseService<Long, ApartmentDto>
        implements IGetApartmentDetailService<Long, ApartmentDto> {

    @Autowired
    ApartmentMapper apartmentMapper;

    @Autowired
    ApartmentRepository apartmentRepository;

    @Override
    public void preExecute(Long id) {
        Apartment apartment = apartmentRepository.findById(id).orElse(null);
        if (apartment == null){
            throw new NotFoundException(messageHelper.getMessage(MessageCode.Apartment.APARTMENT_NOT_FOUND));
        }
    }

    @Override
    public ApartmentDto doing(Long id) {
        log.info("Get detail apartment ID: " + id);

        return apartmentMapper.toApartmentFullDto(apartmentRepository.findById(id).get());
    }
}
