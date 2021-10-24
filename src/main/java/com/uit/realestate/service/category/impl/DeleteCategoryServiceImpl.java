package com.uit.realestate.service.category.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.domain.apartment.Category;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.repository.category.CategoryRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.category.IDeleteCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeleteCategoryServiceImpl extends AbstractBaseService<Long, Boolean>
        implements IDeleteCategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Boolean doing(Long categoryId) {
        log.info("Delete category Id:" + categoryId);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.Category.NOT_FOUND)));
        category.setDeleted(true);
        categoryRepository.save(category);
        return true;
    }
}
