package com.uit.realestate.service.apartment;

import com.uit.realestate.dto.apartment.ApartmentBasicDto;
import com.uit.realestate.dto.apartment.ApartmentCompareDto;
import com.uit.realestate.dto.apartment.ApartmentDto;
import com.uit.realestate.dto.apartment.ApartmentSearchDto;
import com.uit.realestate.dto.response.PaginationResponse;
import com.uit.realestate.payload.CatchInfoRequest;
import com.uit.realestate.payload.CatchInfoRequestExt;
import com.uit.realestate.payload.apartment.*;

import java.util.List;

public interface ApartmentService {

    boolean addApartment(AddApartmentRequest req);

    boolean closeApartmentService(Long apartmentId);

    List<ApartmentCompareDto> compareApartment(CompareApartmentRequest req);

    boolean favouriteApartment(FavouriteApartmentRequest req);

    List<ApartmentBasicDto> findHighLightApartment(CatchInfoRequest req);

    List<ApartmentBasicDto> findLatestApartment(CatchInfoRequest req);

    PaginationResponse<ApartmentBasicDto> findRecommendApartment(CatchInfoRequestExt req);

    PaginationResponse<ApartmentBasicDto> findSimilarApartment(CatchInfoRequestExt req);

    ApartmentDto getApartmentDetail(DetailApartmentRequest req);

    boolean highLightApartment(Long apartmentId);

    List<ApartmentSearchDto> searchAllApartment(String req);

    PaginationResponse<ApartmentDto> searchApartment(SearchApartmentRequest req);

    boolean updateApartment(UpdateApartmentRequest req);

    boolean validateApartment(ValidateApartmentRequest req);

    boolean existApartment(String title);

    Long findByTitleNewest(String title);

    void deletePermanent(String title);

    PaginationResponse<ApartmentBasicDto> findApartmentWithSuitable(Long userId);
}
