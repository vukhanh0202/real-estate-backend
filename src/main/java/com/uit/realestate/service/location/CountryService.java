package com.uit.realestate.service.location;

import com.uit.realestate.dto.location.CountryDto;

import java.util.List;

public interface CountryService {

    List<CountryDto> findAll();
}
