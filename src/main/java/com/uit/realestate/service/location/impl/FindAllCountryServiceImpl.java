package com.uit.realestate.service.location.impl;

import com.uit.realestate.domain.location.Province;
import com.uit.realestate.dto.location.CountryDto;
import com.uit.realestate.mapper.location.CountryMapper;
import com.uit.realestate.mapper.location.ProvinceMapper;
import com.uit.realestate.repository.location.CountryRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.location.IFindAllCountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FindAllCountryServiceImpl extends AbstractBaseService<Void, List<CountryDto>>
        implements IFindAllCountryService<Void, List<CountryDto>> {

    @Autowired
    private CountryMapper countryMapper;

    @Autowired
    private CountryRepository countryRepository;

    @Override
    public List<CountryDto> doing(Void unused) {
        log.info("FindAllCountryServiceImpl: find all country");

        return countryMapper.toCountryDtoList(countryRepository.findAll());
    }
}
