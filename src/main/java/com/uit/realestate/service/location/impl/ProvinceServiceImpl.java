package com.uit.realestate.service.location.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.domain.location.District;
import com.uit.realestate.domain.location.Province;
import com.uit.realestate.dto.location.ProvinceDto;
import com.uit.realestate.exception.InvalidException;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.mapper.location.ProvinceMapper;
import com.uit.realestate.repository.location.ProvinceRepository;
import com.uit.realestate.service.location.ProvinceService;
import com.uit.realestate.utils.MessageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ProvinceServiceImpl implements ProvinceService {

    private final ProvinceRepository provinceRepository;
    private final MessageHelper messageHelper;
    private final ProvinceMapper provinceMapper;

    @Override
    public void validationProvince(Long provinceId) {
        if (provinceId == null) {
            throw new InvalidException(messageHelper.getMessage(MessageCode.Province.INVALID));
        }

        provinceRepository.findById(provinceId)
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.Province.INVALID)));
    }

    @Override
    public List<ProvinceDto> findAllProvinceByCountryCode(String countryCode) {
        return provinceMapper.toProvinceDtoList(provinceRepository.findAllByCountry_Code(countryCode));
    }

    @Override
    public ProvinceDto findByDistrict(Long districtId) {
        return provinceMapper.toProvinceDto(provinceRepository.findByDistrictId(districtId)
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.Province.INVALID))));
    }

    @Override
    public ProvinceDto findById(Long id) {
        Province province = provinceRepository.findById(id).orElse(null);
        if (province == null){
            ProvinceDto dto = new ProvinceDto();
            dto.setName("");
            return dto;
        }
        return provinceMapper.toProvinceDto(province);
    }
}
