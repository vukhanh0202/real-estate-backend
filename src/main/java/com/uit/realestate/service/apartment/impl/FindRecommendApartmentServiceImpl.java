package com.uit.realestate.service.apartment.impl;

import com.uit.realestate.domain.tracking.TrackingCategory;
import com.uit.realestate.domain.tracking.TrackingDistrict;
import com.uit.realestate.domain.tracking.TrackingProvince;
import com.uit.realestate.dto.apartment.ApartmentBasicDto;
import com.uit.realestate.mapper.apartment.ApartmentMapper;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.repository.tracking.TrackingCategoryRepository;
import com.uit.realestate.repository.tracking.TrackingDistrictRepository;
import com.uit.realestate.repository.tracking.TrackingProvinceRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.apartment.IFindHighlightApartmentService;
import com.uit.realestate.service.apartment.IFindRecommendApartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FindRecommendApartmentServiceImpl extends AbstractBaseService<IFindRecommendApartmentService.Input, List<ApartmentBasicDto>>
        implements IFindRecommendApartmentService<IFindRecommendApartmentService.Input, List<ApartmentBasicDto>> {

    @Autowired
    private ApartmentMapper apartmentMapper;

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private TrackingCategoryRepository trackingCategoryRepository;

    @Autowired
    private TrackingProvinceRepository trackingProvinceRepository;

    @Autowired
    private TrackingDistrictRepository trackingDistrictRepository;

    @Override
    public List<ApartmentBasicDto> doing(Input input) {
        log.info("Find recommend apartment");
        List<TrackingCategory> trackingCategories = trackingCategoryRepository.findAllByUserIdAndIp(input.getUserId(), input.getIp());
        List<TrackingDistrict> trackingDistricts = trackingDistrictRepository.findAllByUserIdAndIp(input.getUserId(), input.getIp());
        List<TrackingProvince> trackingProvinces = trackingProvinceRepository.findAllByUserIdAndIp(input.getUserId(), input.getIp());

        return apartmentMapper.toApartmentBasicDtoList(apartmentRepository.findTop4ByHighlightTrueOrderByUpdatedAtDesc());
    }
}
