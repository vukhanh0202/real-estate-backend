package com.uit.realestate.service.category.impl;

import com.uit.realestate.service.category.*;
import com.uit.realestate.service.location.IFindAllCountryService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
@Slf4j
public class CategoryService implements ICategoryService {

    private final IFindAllCategoryService findAllCategoryService;

    private final ICreateCategoryService createCategoryService;

    private final IUpdateCategoryService updateCategoryService;

    private final IDeleteCategoryService deleteCategoryService;

    public CategoryService(IFindAllCategoryService findAllCategoryService, ICreateCategoryService createCategoryService, IUpdateCategoryService updateCategoryService, IDeleteCategoryService deleteCategoryService) {
        this.findAllCategoryService = findAllCategoryService;
        this.createCategoryService = createCategoryService;
        this.updateCategoryService = updateCategoryService;
        this.deleteCategoryService = deleteCategoryService;
    }
}
