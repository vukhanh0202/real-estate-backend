package com.uit.realestate.controller.dashboard;

import com.uit.realestate.constant.AppConstant;
import com.uit.realestate.constant.enums.sort.ESortCategory;
import com.uit.realestate.dto.response.ApiResponse;
import com.uit.realestate.payload.category.CategoryRequest;
import com.uit.realestate.service.category.ICreateCategoryService;
import com.uit.realestate.service.category.IDeleteCategoryService;
import com.uit.realestate.service.category.IFindAllCategoryPaginationService;
import com.uit.realestate.service.category.IUpdateCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Api(value = "Category Dashboard APIs")
@RequestMapping("/dashboard/category")
@RequiredArgsConstructor
public class DashboardCategoryController {

    private final IFindAllCategoryPaginationService findAllCategoryPaginationService;

    private final ICreateCategoryService createCategoryService;

    private final IUpdateCategoryService updateCategoryService;

    private final IDeleteCategoryService deleteCategoryService;

    @ApiOperation(value = "Find All Category", authorizations = {@Authorization(value = "JWT")})
    @GetMapping(value = "")
    @PreAuthorize("@securityService.hasRoles('ADMIN')")
    public ResponseEntity<?> findAllCategory(@RequestParam(value = "page", defaultValue = AppConstant.PAGE_NUMBER_DEFAULT) Integer page,
                                             @RequestParam(value = "size", defaultValue = AppConstant.PAGE_SIZE_DEFAULT) Integer size,
                                             @RequestParam(value = "sort_by", defaultValue = "ID") ESortCategory sortBy,
                                             @RequestParam(value = "sort_direction", defaultValue = "DESC") Sort.Direction sortDirection,
                                             @RequestParam(value = "search", defaultValue = "") String search){
        IFindAllCategoryPaginationService.Input input = new IFindAllCategoryPaginationService.Input(search, page, size);
        input.createPageable(sortDirection, sortBy.getValue());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(findAllCategoryPaginationService.execute(input)));
    }

    @ApiOperation(value = "Create Category", authorizations = {@Authorization(value = "JWT")})
    @PostMapping(value = "/create")
    @PreAuthorize("@securityService.hasRoles('ADMIN')")
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest categoryRequest){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(createCategoryService.execute(categoryRequest)));
    }

    @ApiOperation(value = "Update Category", authorizations = {@Authorization(value = "JWT")})
    @PutMapping(value = "/update/{id}")
    @PreAuthorize("@securityService.hasRoles('ADMIN')")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Long id,
                                            @RequestBody CategoryRequest categoryRequest){
        categoryRequest.setId(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(updateCategoryService.execute(categoryRequest)));
    }

    @ApiOperation(value = "Delete Category", authorizations = {@Authorization(value = "JWT")})
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("@securityService.hasRoles('ADMIN')")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(deleteCategoryService.execute(id)));
    }
}
