package com.uit.realestate.controller.dashboard;

import com.uit.realestate.dto.response.ApiResponse;
import com.uit.realestate.payload.category.CategoryRequest;
import com.uit.realestate.service.category.ICategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
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

    @ApiOperation(value = "Find All Category", authorizations = {@Authorization(value = "JWT")})
    @GetMapping(value = "")
    public ResponseEntity<?> findAllCategory(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(categoryService.getFindAllCategoryService()
                        .execute()));
    }

    @ApiOperation(value = "Create Category", authorizations = {@Authorization(value = "JWT")})
    @PostMapping(value = "/create")
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest categoryRequest){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(categoryService.getCreateCategoryService()
                        .execute(categoryRequest)));
    }

    @ApiOperation(value = "Update Category", authorizations = {@Authorization(value = "JWT")})
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Long id,
                                            @RequestBody CategoryRequest categoryRequest){
        categoryRequest.setId(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(categoryService.getUpdateCategoryService()
                        .execute(categoryRequest)));
    }

    @ApiOperation(value = "Delete Category", authorizations = {@Authorization(value = "JWT")})
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(categoryService.getDeleteCategoryService()
                        .execute(id)));
    }
}
