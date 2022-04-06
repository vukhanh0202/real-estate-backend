package com.uit.realestate.payload;

import com.uit.realestate.dto.response.PaginationRequest;
import lombok.Data;

@Data
public class CatchInfoRequestExt extends PaginationRequest {

    protected Long userId;
    protected String ip;

    public CatchInfoRequestExt() {
    }

    public CatchInfoRequestExt(Long userId) {
        this.userId = userId;
    }

    public CatchInfoRequestExt(String ip) {
        this.ip = ip;
    }

    public CatchInfoRequestExt(Long userId, String ip) {
        this.userId = userId;
        this.ip = ip;
    }

    public CatchInfoRequestExt(Long userId, Integer page, Integer size) {
        super(page, size);
        this.userId = userId;
    }

    public CatchInfoRequestExt(String ip, Integer page, Integer size) {
        super(page, size);
        this.ip = ip;
    }

    public CatchInfoRequestExt(Long userId, String ip, Integer page, Integer size) {
        super(page, size);
        this.userId = userId;
        this.ip = ip;
    }

    public CatchInfoRequestExt(Integer page, Integer size) {
        super(page, size);
    }
}
