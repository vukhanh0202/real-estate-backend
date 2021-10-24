package com.uit.realestate.service.location;

import com.uit.realestate.dto.location.ProvinceDto;
import com.uit.realestate.service.IService;

import java.util.List;

public interface IFindAllProvinceByCountryCodeService extends IService<String, List<ProvinceDto>> {
}
