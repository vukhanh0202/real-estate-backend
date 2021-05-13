package com.uit.realestate.service.category.impl;

import com.uit.realestate.dto.category.CategoryDto;
import com.uit.realestate.dto.location.CountryDto;
import com.uit.realestate.mapper.category.CategoryMapper;
import com.uit.realestate.mapper.location.CountryMapper;
import com.uit.realestate.repository.category.CategoryRepository;
import com.uit.realestate.repository.location.CountryRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.category.IFindAllCategoryService;
import com.uit.realestate.service.location.IFindAllCountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FindAllCategoryServiceImpl extends AbstractBaseService<Void, List<CategoryDto>>
        implements IFindAllCategoryService<Void, List<CategoryDto>> {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> doing(Void unused) {
        log.info("FindAllCategoryServiceImpl: find all category");

        return categoryMapper.toCategoryDtoList(categoryRepository.findAll());
    }
}
