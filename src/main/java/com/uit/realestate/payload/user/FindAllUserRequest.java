package com.uit.realestate.payload.user;

import com.uit.realestate.dto.response.PaginationRequest;
import lombok.Data;

@Data
public class FindAllUserRequest extends PaginationRequest {

    private String search;

    public FindAllUserRequest(Integer page, Integer size, String search) {
        super(page, size);
        this.search = search;
    }
}
