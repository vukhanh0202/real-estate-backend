package com.uit.realestate.service.location;

import com.uit.realestate.dto.location.DistrictDto;

import java.util.List;

public interface DistrictService {

    void validationDistrict(Long districtId, Long provinceId);

    List<DistrictDto> findAllDistrictByProvince(Long provinceId);

    DistrictDto findDistrictNameIn(String str);
}
