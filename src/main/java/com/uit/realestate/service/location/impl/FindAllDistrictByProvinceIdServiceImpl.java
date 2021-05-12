package com.uit.realestate.service.location.impl;

import com.uit.realestate.dto.location.DistrictDto;
import com.uit.realestate.dto.location.ProvinceDto;
import com.uit.realestate.mapper.location.DistrictMapper;
import com.uit.realestate.mapper.location.ProvinceMapper;
import com.uit.realestate.repository.location.DistrictRepository;
import com.uit.realestate.repository.location.ProvinceRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.location.IFindAllDistrictByProvinceIdService;
import com.uit.realestate.service.location.IFindAllProvinceByCountryCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FindAllDistrictByProvinceIdServiceImpl extends AbstractBaseService<Long, List<DistrictDto>>
        implements IFindAllDistrictByProvinceIdService<Long, List<DistrictDto>> {

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private DistrictMapper districtMapper;

    @Override
    public List<DistrictDto> doing(Long provinceId) {
        log.info("FindAllDistrictByProvinceIdServiceImpl: find district by province ID");

        return districtMapper.toProvinceDtoList(districtRepository.findAllByProvince_Id(provinceId));
    }
}
