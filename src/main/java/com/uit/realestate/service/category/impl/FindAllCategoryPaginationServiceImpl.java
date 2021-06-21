package com.uit.realestate.service.category.impl;

import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.domain.apartment.Category;
import com.uit.realestate.dto.category.CategoryDto;
import com.uit.realestate.dto.response.PaginationResponse;
import com.uit.realestate.mapper.category.CategoryMapper;
import com.uit.realestate.repository.category.CategoryRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.category.IFindAllCategoryPaginationService;
import com.uit.realestate.service.category.IFindAllCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FindAllCategoryPaginationServiceImpl extends AbstractBaseService<IFindAllCategoryPaginationService.Input, PaginationResponse<CategoryDto>>
        implements IFindAllCategoryPaginationService<IFindAllCategoryPaginationService.Input, PaginationResponse<CategoryDto>> {

    private final CategoryMapper categoryMapper;

    private final CategoryRepository categoryRepository;

    public FindAllCategoryPaginationServiceImpl(CategoryMapper categoryMapper, CategoryRepository categoryRepository) {
        this.categoryMapper = categoryMapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public PaginationResponse<CategoryDto> doing(Input input) {
        log.info("FindAllCategoryServiceImpl: find all category");
        Page<Category> result = categoryRepository.findAllByNameContainingAndIsDeletedFalse(input.getSearch(), input.getPageable());
        return new PaginationResponse(
                result.getTotalElements()
                , result.getNumberOfElements()
                , result.getNumber() + 1
                , categoryMapper.toCategoryDtoList(result.getContent()));
    }
}
