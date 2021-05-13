package com.uit.realestate.service.apartment;

import com.uit.realestate.dto.apartment.ApartmentDto;

import java.util.List;

public interface IApartmentService {

    ISearchApartmentService<ISearchApartmentService.Input, List<ApartmentDto>> getSearchApartmentService();

    IGetApartmentDetailService<Long, ApartmentDto> getGetApartmentDetailService();
}
