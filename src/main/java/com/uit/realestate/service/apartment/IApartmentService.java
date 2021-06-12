package com.uit.realestate.service.apartment;

import com.uit.realestate.dto.apartment.ApartmentBasicDto;
import com.uit.realestate.dto.apartment.ApartmentDto;
import com.uit.realestate.dto.response.PaginationResponse;
import com.uit.realestate.payload.apartment.AddApartmentRequest;
import com.uit.realestate.payload.apartment.ApartmentSearch;
import com.uit.realestate.payload.apartment.UpdateApartmentRequest;

import java.util.List;

public interface IApartmentService {

    ISearchApartmentService<ISearchApartmentService.Input, PaginationResponse<ApartmentDto>> getSearchApartmentService();

    IGetApartmentDetailService<IGetApartmentDetailService.Input, ApartmentDto> getGetApartmentDetailService();

    IAddApartmentService<AddApartmentRequest, Boolean> getAddApartmentService();

    IFindLatestNewApartmentService<Long, List<ApartmentBasicDto>> getFindLatestNewApartmentService();

    IFindHighlightApartmentService<Long, List<ApartmentBasicDto>> getFindHighlightApartmentService();

    IValidateApartmentService<IValidateApartmentService.Input, Boolean> getValidateApartmentService();

    IUpdateApartmentService<UpdateApartmentRequest, Boolean> getUpdateApartmentService();

    ICloseApartmentService<Long, Boolean> getCloseApartmentService();

    IFindRecommendApartmentService<IFindRecommendApartmentService.Input, PaginationResponse<ApartmentBasicDto>> getFindRecommendApartmentService();

    IFavouriteApartmentService<IFavouriteApartmentService.Input, Boolean> getFavouriteApartmentService();
}
