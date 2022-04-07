package com.uit.realestate.service.location.impl;

import com.uit.realestate.dto.location.CountryDto;
import com.uit.realestate.mapper.location.CountryMapper;
import com.uit.realestate.repository.location.CountryRepository;
import com.uit.realestate.service.location.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryMapper countryMapper;
    private final CountryRepository countryRepository;

    @Override
    public List<CountryDto> findAll() {
        return countryMapper.toCountryDtoList(countryRepository.findAll());
    }
}
