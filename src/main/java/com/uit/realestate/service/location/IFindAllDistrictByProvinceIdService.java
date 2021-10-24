package com.uit.realestate.service.location;

import com.uit.realestate.dto.location.DistrictDto;
import com.uit.realestate.service.IService;

import java.util.List;

public interface IFindAllDistrictByProvinceIdService extends IService<Long, List<DistrictDto>> {
}
