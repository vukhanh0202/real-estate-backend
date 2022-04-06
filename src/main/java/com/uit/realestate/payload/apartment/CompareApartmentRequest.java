package com.uit.realestate.payload.apartment;

import com.uit.realestate.payload.CatchInfoRequest;
import lombok.Data;

import java.util.List;

@Data
public class CompareApartmentRequest extends CatchInfoRequest {

    List<Long> ids;

    public CompareApartmentRequest(List<Long> ids, Long userId) {
        super(userId);
        this.ids = ids;
    }
}
