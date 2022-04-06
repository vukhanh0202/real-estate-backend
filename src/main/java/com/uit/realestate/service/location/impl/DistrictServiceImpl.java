package com.uit.realestate.service.location.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.domain.location.District;
import com.uit.realestate.exception.InvalidException;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.repository.location.DistrictRepository;
import com.uit.realestate.service.location.DistrictService;
import com.uit.realestate.service.location.ProvinceService;
import com.uit.realestate.utils.MessageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DistrictServiceImpl implements DistrictService {

    private final DistrictRepository districtRepository;

    private final ProvinceService provinceService;

    private final MessageHelper messageHelper;

    @Override
    public void validationDistrict(Long districtId, Long provinceId) {
        if (districtId == null) {
            throw new InvalidException(messageHelper.getMessage(MessageCode.District.INVALID));
        }
        District district = districtRepository.findById(districtId)
                .orElseThrow(() -> new NotFoundException(MessageCode.District.INVALID));
        provinceService.validationProvince(provinceId);

        if (!district.getProvince().getId().equals(provinceId)) {
            throw new InvalidException(messageHelper.getMessage(MessageCode.Address.INVALID));
        }
    }
}
