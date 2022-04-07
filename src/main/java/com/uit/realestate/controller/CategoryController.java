package com.uit.realestate.controller;

import com.uit.realestate.dto.response.ApiResponse;
import com.uit.realestate.service.category.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Api(value = "Category APIs")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @ApiOperation(value = "Find All Category")
    @GetMapping(value = "/public/all-category")
    public ResponseEntity<?> findAllCategory(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(categoryService.findAll()));
    }
}
