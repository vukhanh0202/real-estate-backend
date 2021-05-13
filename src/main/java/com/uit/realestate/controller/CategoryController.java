package com.uit.realestate.controller;

import com.uit.realestate.dto.response.ApiResponse;
import com.uit.realestate.service.category.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @GetMapping(value = "/public/all-category")
    public ResponseEntity<?> findAllCategory(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(categoryService.getFindAllCategoryService()
                        .execute()));
    }
}
