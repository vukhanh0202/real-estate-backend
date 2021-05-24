package com.uit.realestate.service.apartment;

import com.uit.realestate.dto.apartment.ApartmentBasicDto;
import com.uit.realestate.dto.apartment.ApartmentDto;
import com.uit.realestate.payload.apartment.AddApartmentRequest;

import java.util.List;

public interface IApartmentService {

    ISearchApartmentService<ISearchApartmentService.Input, List<ApartmentDto>> getSearchApartmentService();

    IGetApartmentDetailService<Long, ApartmentDto> getGetApartmentDetailService();

    IAddApartmentService<AddApartmentRequest, Boolean> getAddApartmentService();

    IFindLatestNewApartmentService<Void, List<ApartmentBasicDto>> getFindLatestNewApartmentService();
}
