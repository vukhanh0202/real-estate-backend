package com.uit.realestate.service.location.impl;

import com.uit.realestate.dto.location.DistrictDto;
import com.uit.realestate.mapper.location.DistrictMapper;
import com.uit.realestate.repository.location.DistrictRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.location.IFindAllDistrictByProvinceIdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FindAllDistrictByProvinceIdServiceImpl extends AbstractBaseService<Long, List<DistrictDto>>
        implements IFindAllDistrictByProvinceIdService {

    private final DistrictRepository districtRepository;

    private final DistrictMapper districtMapper;

    @Override
    public List<DistrictDto> doing(Long provinceId) {
        log.info("FindAllDistrictByProvinceIdServiceImpl: find district by province ID");

        return districtMapper.toProvinceDtoList(districtRepository.findAllByProvince_Id(provinceId));
    }
}
