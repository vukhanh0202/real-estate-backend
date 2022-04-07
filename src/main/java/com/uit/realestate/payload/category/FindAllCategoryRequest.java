package com.uit.realestate.payload.category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.uit.realestate.dto.response.PaginationRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FindAllCategoryRequest extends PaginationRequest {

    private String search;

    public FindAllCategoryRequest(String search, Integer page, Integer size){
        super(page, size);
        this.search = search;
    }
}
