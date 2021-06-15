package com.uit.realestate.service.apartment.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.domain.user.User;
import com.uit.realestate.exception.InvalidException;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.mapper.apartment.ApartmentMapper;
import com.uit.realestate.payload.apartment.AddApartmentRequest;
import com.uit.realestate.payload.apartment.UpdateApartmentRequest;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.repository.category.CategoryRepository;
import com.uit.realestate.repository.location.DistrictRepository;
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

    private final DistrictRepository districtRepository;

    public UpdateApartmentServiceImpl(ApartmentMapper apartmentMapper, ApartmentRepository apartmentRepository, CategoryRepository categoryRepository, DistrictRepository districtRepository) {
        this.apartmentMapper = apartmentMapper;
        this.apartmentRepository = apartmentRepository;
        this.categoryRepository = categoryRepository;
        this.districtRepository = districtRepository;
    }

    @Override
    public void preExecute(UpdateApartmentRequest updateApartmentRequest) {

        Apartment apartment = apartmentRepository.findById(updateApartmentRequest.getId())
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.Apartment.NOT_FOUND)));
        if (!updateApartmentRequest.getIsAdmin() && !updateApartmentRequest.getAuthorId().equals(apartment.getAuthor().getId())) {
            throw new NotFoundException(messageHelper.getMessage(MessageCode.Token.NOT_PERMISSION));
        }
        if (!updateApartmentRequest.getIsAdmin() && !apartment.getStatus().equals(EApartmentStatus.PENDING)) {
            throw new NotFoundException(messageHelper.getMessage(MessageCode.Token.NOT_PERMISSION));
        }
        if (updateApartmentRequest.getCategoryId() != null && categoryRepository.findById(updateApartmentRequest.getId()).isEmpty()) {
            throw new NotFoundException(messageHelper.getMessage(MessageCode.Category.NOT_FOUND));
        }
        if (updateApartmentRequest.getApartmentAddress() != null) {
            Long districtId = updateApartmentRequest.getApartmentAddress().getDistrictId();
            Long provinceId = updateApartmentRequest.getApartmentAddress().getProvinceId();
            if (districtId == null) {
                districtId = apartment.getApartmentAddress().getDistrict().getId();
            }
            if (provinceId == null) {
                provinceId = apartment.getApartmentAddress().getProvince().getId();
            }
            if (!districtRepository.findById(districtId).get().getProvince().getId().equals(provinceId)) {
                throw new InvalidException(messageHelper.getMessage(MessageCode.Address.INVALID));
            }
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
