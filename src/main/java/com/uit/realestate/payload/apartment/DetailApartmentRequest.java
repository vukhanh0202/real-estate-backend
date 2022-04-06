package com.uit.realestate.payload.apartment;

import com.uit.realestate.payload.CatchInfoRequest;
import lombok.Data;

@Data
public class DetailApartmentRequest extends CatchInfoRequest {

    private Long id;

    public DetailApartmentRequest(Long id, String ip,Long userId) {
        super(userId, ip);
        this.id = id;
    }
}
