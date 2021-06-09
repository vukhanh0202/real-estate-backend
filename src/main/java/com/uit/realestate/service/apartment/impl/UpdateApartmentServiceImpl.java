package com.uit.realestate.service.apartment.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.mapper.apartment.ApartmentMapper;
import com.uit.realestate.payload.apartment.AddApartmentRequest;
import com.uit.realestate.payload.apartment.UpdateApartmentRequest;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.repository.category.CategoryRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.apartment.IAddApartmentService;
import com.uit.realestate.service.apartment.IUpdateApartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UpdateApartmentServiceImpl extends AbstractBaseService<UpdateApartmentRequest, Boolean>
        implements IUpdateApartmentService<UpdateApartmentRequest, Boolean> {

    private final ApartmentMapper apartmentMapper;

    private final ApartmentRepository apartmentRepository;

    private final CategoryRepository categoryRepository;

    public UpdateApartmentServiceImpl(ApartmentMapper apartmentMapper, ApartmentRepository apartmentRepository, CategoryRepository categoryRepository) {
        this.apartmentMapper = apartmentMapper;
        this.apartmentRepository = apartmentRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void preExecute(UpdateApartmentRequest updateApartmentRequest) {

        Apartment apartment = apartmentRepository.findById(updateApartmentRequest.getId())
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.Apartment.NOT_FOUND)));
        if (updateApartmentRequest.getAuthorId() != apartment.getAuthor().getId()) {
            throw new NotFoundException(messageHelper.getMessage(MessageCode.Token.NOT_PERMISSION));
        }
        if (updateApartmentRequest.getCategoryId() != null && categoryRepository.findById(updateApartmentRequest.getId()).isEmpty()) {
            throw new NotFoundException(messageHelper.getMessage(MessageCode.Category.NOT_FOUND));
        }
    }

    @Override
    public Boolean doing(UpdateApartmentRequest updateApartmentRequest) {
        log.info("Update apartment");
        Apartment apartment = apartmentRepository.findById(updateApartmentRequest.getId()).get();
        apartmentMapper.updateApartment(updateApartmentRequest, apartment);
        apartmentRepository.save(apartment);
        return true;
    }
}
