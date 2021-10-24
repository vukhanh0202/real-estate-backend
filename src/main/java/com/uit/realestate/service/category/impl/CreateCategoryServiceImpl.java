package com.uit.realestate.service.category.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.domain.apartment.Category;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.mapper.category.CategoryMapper;
import com.uit.realestate.payload.category.CategoryRequest;
import com.uit.realestate.repository.category.CategoryRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.category.ICreateCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateCategoryServiceImpl extends AbstractBaseService<CategoryRequest, Boolean>
        implements ICreateCategoryService {

    private final CategoryMapper categoryMapper;

    private final CategoryRepository categoryRepository;

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
