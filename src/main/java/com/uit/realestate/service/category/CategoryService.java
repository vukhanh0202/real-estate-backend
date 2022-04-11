package com.uit.realestate.service.category;

import com.uit.realestate.dto.category.CategoryDto;
import com.uit.realestate.dto.response.PaginationResponse;
import com.uit.realestate.payload.category.CategoryRequest;
import com.uit.realestate.payload.category.FindAllCategoryRequest;

import java.util.List;

public interface CategoryService {

    boolean createCategory(CategoryRequest req);

    boolean updateCategory(CategoryRequest req);

    boolean deleteCategory(Long id);

    PaginationResponse<CategoryDto> findAll(FindAllCategoryRequest req);

    List<CategoryDto> findAll();

    CategoryDto findOrCreate(String name);
}
