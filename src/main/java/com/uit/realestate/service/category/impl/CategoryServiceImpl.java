package com.uit.realestate.service.category.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.domain.apartment.Category;
import com.uit.realestate.dto.category.CategoryDto;
import com.uit.realestate.dto.response.PaginationResponse;
import com.uit.realestate.exception.InvalidException;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.mapper.category.CategoryMapper;
import com.uit.realestate.payload.category.CategoryRequest;
import com.uit.realestate.payload.category.FindAllCategoryRequest;
import com.uit.realestate.repository.category.CategoryRepository;
import com.uit.realestate.service.category.CategoryService;
import com.uit.realestate.utils.MessageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    private final MessageHelper messageHelper;

    @Override
    public boolean createCategory(CategoryRequest req) {
        if (categoryRepository.findByNameAndIsDeletedFalse(req.getName()).isPresent()) {
            throw new NotFoundException(messageHelper.getMessage(MessageCode.Category.EXIST));
        }

        log.info("Create new category with name: " + req.getName());

        Category category = categoryMapper.toCategory(req);

        categoryRepository.save(category);
        return true;
    }

    @Override
    public boolean updateCategory(CategoryRequest req) {
        Category category = categoryRepository.findById(req.getId())
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.Category.NOT_FOUND)));

        log.info("Update category");

        categoryMapper.updateCategory(req, category);

        categoryRepository.save(category);
        return true;
    }

    @Override
    public boolean deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.Category.NOT_FOUND)));

        log.info("Delete category Id:" + id);

        category.setDeleted(true);
        categoryRepository.save(category);
        return true;
    }

    @Override
    public PaginationResponse<CategoryDto> findAll(FindAllCategoryRequest req) {
        Page<Category> result = categoryRepository.findAllByNameContainingAndIsDeletedFalse(req.getSearch(), req.getPageable());

        return new PaginationResponse<>(
                result.getTotalElements()
                , result.getNumberOfElements()
                , result.getNumber() + 1
                , categoryMapper.toCategoryDtoList(result.getContent()));
    }

    @Override
    public List<CategoryDto> findAll() {
        List<CategoryDto> result = categoryMapper.toCategoryDtoList(categoryRepository.findAllByIsDeletedFalse());
        result.sort((o1, o2) -> {
            // sort DESC
            if (o2.getTotalItem() > (o1.getTotalItem())) {
                return 1;
            } else if (o1.getTotalItem() > (o2.getTotalItem())) {
                return -1;
            }
            return 0;
        });
        return result;
    }

    @Override
    public CategoryDto findOrCreate(String name) {
        Category category = categoryRepository.findByNameAndIsDeletedFalse(name).orElse(null);
        if (Objects.isNull(category)) {
            boolean res = createCategory(CategoryRequest.builder().name(name).build());
            if (!res){
                throw new InvalidException(messageHelper.getMessage(MessageCode.Category.NOT_FOUND));
            }
            category = categoryRepository.findByNameAndIsDeletedFalse(name).orElseThrow(() -> new InvalidException(messageHelper.getMessage(MessageCode.Category.NOT_FOUND)));;
        }
        return CategoryDto.builder().id(category.getId()).build();
    }
}
