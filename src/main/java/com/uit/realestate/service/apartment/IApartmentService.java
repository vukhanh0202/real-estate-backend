package com.uit.realestate.service.apartment;

import com.uit.realestate.dto.apartment.ApartmentBasicDto;
import com.uit.realestate.dto.location.CountryDto;
import com.uit.realestate.dto.location.DistrictDto;
import com.uit.realestate.dto.location.ProvinceDto;
import com.uit.realestate.service.location.IFindAllCountryService;
import com.uit.realestate.service.location.IFindAllDistrictByProvinceIdService;
import com.uit.realestate.service.location.IFindAllProvinceByCountryCodeService;

import java.util.List;

public interface IApartmentService {

    ISearchApartmentService<ISearchApartmentService.Input, List<ApartmentBasicDto>> getSearchApartmentService();

}
