package com.uit.realestate.payload.apartment;

import com.uit.realestate.payload.CatchInfoRequest;
import lombok.Data;

@Data
public class FavouriteApartmentRequest extends CatchInfoRequest {

    private Long apartmentId;

    public FavouriteApartmentRequest(Long userId, Long apartmentId,String ip) {
        super(userId, ip);
        this.apartmentId = apartmentId;
    }
}
