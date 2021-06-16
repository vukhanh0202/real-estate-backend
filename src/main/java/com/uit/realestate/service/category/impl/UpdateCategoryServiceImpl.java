package com.uit.realestate.service.category.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.domain.apartment.Category;
import com.uit.realestate.dto.category.CategoryDto;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.mapper.category.CategoryMapper;
import com.uit.realestate.payload.category.CategoryRequest;
import com.uit.realestate.repository.category.CategoryRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.category.IFindAllCategoryService;
import com.uit.realestate.service.category.IUpdateCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UpdateCategoryServiceImpl extends AbstractBaseService<CategoryRequest, Boolean>
        implements IUpdateCategoryService<CategoryRequest, Boolean> {

    private final CategoryMapper categoryMapper;

    private final CategoryRepository categoryRepository;

    public UpdateCategoryServiceImpl(CategoryMapper categoryMapper, CategoryRepository categoryRepository) {
        this.categoryMapper = categoryMapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Boolean doing(CategoryRequest categoryRequest) {
        log.info("Update category");

        Category category = categoryRepository.findById(categoryRequest.getId())
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.Category.NOT_FOUND)));
        categoryMapper.updateCategory(categoryRequest, category);

        categoryRepository.save(category);
        return true;
    }
}
