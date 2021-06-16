package com.uit.realestate.controller.dashboard;

import com.uit.realestate.dto.response.ApiResponse;
import com.uit.realestate.payload.category.CategoryRequest;
import com.uit.realestate.service.category.ICategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Api(value = "Category Dashboard APIs")
@RequestMapping("/dashboard/category")
public class DashboardCategoryController {

    @Autowired
    private ICategoryService categoryService;

    @ApiOperation(value = "Find All Category")
    @GetMapping(value = "")
    public ResponseEntity<?> findAllCategory(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(categoryService.getFindAllCategoryService()
                        .execute()));
    }

    @ApiOperation(value = "Create Category")
    @PostMapping(value = "/create")
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest categoryRequest){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(categoryService.getCreateCategoryService()
                        .execute(categoryRequest)));
    }

    @ApiOperation(value = "Update Category")
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Long id,
                                            @RequestBody CategoryRequest categoryRequest){
        categoryRequest.setId(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(categoryService.getUpdateCategoryService()
                        .execute(categoryRequest)));
    }

    @ApiOperation(value = "Find All Category")
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(categoryService.getDeleteCategoryService()
                        .execute(id)));
    }
}
