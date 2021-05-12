package com.uit.realestate.service.location.impl;

import com.uit.realestate.dto.location.ProvinceDto;
import com.uit.realestate.mapper.location.ProvinceMapper;
import com.uit.realestate.repository.location.ProvinceRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.location.IFindAllProvinceByCountryCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FindAllProvinceByCountryCodeServiceImpl extends AbstractBaseService<String, List<ProvinceDto>>
        implements IFindAllProvinceByCountryCodeService<String, List<ProvinceDto>> {

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private ProvinceMapper provinceMapper;

    @Override
    public List<ProvinceDto> doing(String countryCode) {
        log.info("FindAllProvinceByCountryCodeServiceImpl: find all province by country code");

        return provinceMapper.toProvinceDtoList(provinceRepository.findAllByCountry_Code(countryCode));
    }
}
