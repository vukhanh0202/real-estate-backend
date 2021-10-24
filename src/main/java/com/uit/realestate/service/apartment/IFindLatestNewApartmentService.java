package com.uit.realestate.service.apartment;

import com.uit.realestate.dto.apartment.ApartmentBasicDto;
import com.uit.realestate.service.IService;

import java.util.List;

public interface IFindLatestNewApartmentService extends IService<Long, List<ApartmentBasicDto>> {
}
