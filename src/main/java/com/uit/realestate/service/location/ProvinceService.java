package com.uit.realestate.service.location;

import com.uit.realestate.dto.location.ProvinceDto;

import java.util.List;

public interface ProvinceService {

    void validationProvince(Long provinceId);

    List<ProvinceDto> findAllProvinceByCountryCode(String countryCode);

    ProvinceDto findByDistrict(Long districtId);

    ProvinceDto findById(Long id);

}
