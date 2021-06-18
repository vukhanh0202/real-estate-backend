package com.uit.realestate.service.category;

import com.uit.realestate.dto.category.CategoryDto;
import com.uit.realestate.dto.location.CountryDto;
import com.uit.realestate.dto.location.DistrictDto;
import com.uit.realestate.dto.location.ProvinceDto;
import com.uit.realestate.dto.response.PaginationResponse;
import com.uit.realestate.payload.category.CategoryRequest;
import com.uit.realestate.service.location.IFindAllCountryService;
import com.uit.realestate.service.location.IFindAllDistrictByProvinceIdService;
import com.uit.realestate.service.location.IFindAllProvinceByCountryCodeService;

import java.util.List;

public interface ICategoryService {

    IFindAllCategoryService<Void, List<CategoryDto>> getFindAllCategoryService();

    ICreateCategoryService<CategoryRequest, Boolean> getCreateCategoryService();

    IUpdateCategoryService<CategoryRequest, Boolean> getUpdateCategoryService();

    IDeleteCategoryService<Long, Boolean> getDeleteCategoryService();

    IFindAllCategoryPaginationService<IFindAllCategoryPaginationService.Input, PaginationResponse<CategoryDto>> getFindAllCategoryPaginationService();
}
