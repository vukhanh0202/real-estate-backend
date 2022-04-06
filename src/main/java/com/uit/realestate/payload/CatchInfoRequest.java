package com.uit.realestate.payload;

import lombok.Data;

@Data
public class CatchInfoRequest {

    protected Long userId;
    protected String ip;

    public CatchInfoRequest() {
    }

    public CatchInfoRequest(Long userId) {
        this.userId = userId;
    }

    public CatchInfoRequest(String ip) {
        this.ip = ip;
    }

    public CatchInfoRequest(Long userId, String ip) {
        this.userId = userId;
        this.ip = ip;
    }
}
