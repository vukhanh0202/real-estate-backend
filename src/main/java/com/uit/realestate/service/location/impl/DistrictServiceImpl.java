package com.uit.realestate.service.location.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.domain.location.District;
import com.uit.realestate.dto.location.DistrictDto;
import com.uit.realestate.exception.InvalidException;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.mapper.location.DistrictMapper;
import com.uit.realestate.repository.location.DistrictRepository;
import com.uit.realestate.service.location.DistrictService;
import com.uit.realestate.service.location.ProvinceService;
import com.uit.realestate.utils.MessageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class DistrictServiceImpl implements DistrictService {

    private final DistrictRepository districtRepository;
    private final DistrictMapper districtMapper;

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

    @Override
    public List<DistrictDto> findAllDistrictByProvince(Long provinceId) {
        return districtMapper.toProvinceDtoList(districtRepository.findAllByProvince_Id(provinceId));
    }

    @Override
    public DistrictDto findDistrictNameIn(String str) {
        List<District> districts = districtRepository.findAllByNameIn(Stream.of(str.split(",")).map(String::trim).collect(Collectors.toList()));
        if (districts.size() == 1){
            return districtMapper.toProvinceDto(districts.stream().findFirst().get());
        }
        throw new NotFoundException(messageHelper.getMessage(MessageCode.District.INVALID));
    }
}
