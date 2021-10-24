package com.uit.realestate.service.category.impl;

import com.uit.realestate.domain.apartment.Category;
import com.uit.realestate.dto.category.CategoryDto;
import com.uit.realestate.dto.response.PaginationResponse;
import com.uit.realestate.mapper.category.CategoryMapper;
import com.uit.realestate.repository.category.CategoryRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.category.IFindAllCategoryPaginationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FindAllCategoryPaginationServiceImpl extends AbstractBaseService<IFindAllCategoryPaginationService.Input, PaginationResponse<CategoryDto>>
        implements IFindAllCategoryPaginationService {

    private final CategoryMapper categoryMapper;

    private final CategoryRepository categoryRepository;

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
