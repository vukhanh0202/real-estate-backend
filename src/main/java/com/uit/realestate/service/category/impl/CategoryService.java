package com.uit.realestate.service.category.impl;

import com.uit.realestate.service.category.ICategoryService;
import com.uit.realestate.service.category.IFindAllCategoryService;
import com.uit.realestate.service.location.IFindAllCountryService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
@Slf4j
public class CategoryService implements ICategoryService {

    @Autowired
    private IFindAllCategoryService findAllCategoryService;
}
