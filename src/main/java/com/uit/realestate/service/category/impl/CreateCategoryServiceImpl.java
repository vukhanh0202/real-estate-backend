package com.uit.realestate.service.category.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.domain.apartment.Category;
import com.uit.realestate.dto.category.CategoryDto;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.mapper.category.CategoryMapper;
import com.uit.realestate.payload.category.CategoryRequest;
import com.uit.realestate.repository.category.CategoryRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.category.ICreateCategoryService;
import com.uit.realestate.service.category.IFindAllCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CreateCategoryServiceImpl extends AbstractBaseService<CategoryRequest, Boolean>
        implements ICreateCategoryService<CategoryRequest, Boolean> {

    private final CategoryMapper categoryMapper;

    private final CategoryRepository categoryRepository;

    public CreateCategoryServiceImpl(CategoryMapper categoryMapper, CategoryRepository categoryRepository) {
        this.categoryMapper = categoryMapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void preExecute(CategoryRequest categoryRequest) {
        if (categoryRepository.findByNameAndIsDeletedFalse(categoryRequest.getName()).isPresent()) {
            throw new NotFoundException(messageHelper.getMessage(MessageCode.Category.EXIST));
        }
    }

    @Override
    public Boolean doing(CategoryRequest categoryRequest) {
        log.info("Create new category with name: " + categoryRequest.getName());

        Category category = categoryMapper.toCategory(categoryRequest);

        categoryRepository.save(category);
        return true;
    }
}
