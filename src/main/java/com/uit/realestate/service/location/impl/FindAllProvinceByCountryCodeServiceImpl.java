package com.uit.realestate.service.location.impl;

import com.uit.realestate.dto.location.ProvinceDto;
import com.uit.realestate.mapper.location.ProvinceMapper;
import com.uit.realestate.repository.location.ProvinceRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.location.IFindAllProvinceByCountryCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FindAllProvinceByCountryCodeServiceImpl extends AbstractBaseService<String, List<ProvinceDto>>
        implements IFindAllProvinceByCountryCodeService {

    private final ProvinceRepository provinceRepository;

    private final ProvinceMapper provinceMapper;

    @Override
    public List<ProvinceDto> doing(String countryCode) {
        log.info("FindAllProvinceByCountryCodeServiceImpl: find all province by country code");

        return provinceMapper.toProvinceDtoList(provinceRepository.findAllByCountry_Code(countryCode));
    }
}
