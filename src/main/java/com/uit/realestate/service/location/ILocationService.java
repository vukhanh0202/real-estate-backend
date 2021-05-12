package com.uit.realestate.service.location;

import com.uit.realestate.dto.location.CountryDto;
import com.uit.realestate.dto.location.DistrictDto;
import com.uit.realestate.dto.location.ProvinceDto;

import java.util.List;

public interface ILocationService {

    IFindAllCountryService<Void, List<CountryDto>> getFindAllCountryService();

    IFindAllProvinceByCountryCodeService<String, List<ProvinceDto>> getFindAllProvinceByCountryCodeService();

    IFindAllDistrictByProvinceIdService<Long, List<DistrictDto>> getFindAllDistrictByProvinceIdService();
}
