package com.uit.realestate.payload.user;

import com.uit.realestate.dto.response.PaginationRequest;
import lombok.Data;

@Data
public class FindUserApartmentFavouriteRequest extends PaginationRequest {

    private Long userId;
    private String ip;

    public FindUserApartmentFavouriteRequest(Integer page, Integer size, Long userId, String ip) {
        super(page, size);
        this.userId = userId;
        this.ip = ip;
    }
}
