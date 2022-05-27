package com.uit.realestate.payload.user;

import com.uit.realestate.dto.response.PaginationRequest;
import lombok.Data;

@Data
public class FindUserApartmentAuthorRequest extends PaginationRequest {

    private Long userId;
    private String ip;

    public FindUserApartmentAuthorRequest(Integer page, Integer size, Long userId,String ip) {
        super(page, size);
        this.userId = userId;
        this.ip = ip;
    }
}
