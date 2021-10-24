package com.uit.realestate.service.user;

import com.uit.realestate.dto.response.FileCaption;
import com.uit.realestate.dto.response.PaginationRequest;
import com.uit.realestate.payload.user.UpdateAvatarUserRequest;
import com.uit.realestate.service.IService;
import lombok.Data;

import java.util.Set;

public interface IUpdateAvatarUserService extends IService<UpdateAvatarUserRequest, Set<FileCaption>> {

    @Data
    class Input extends PaginationRequest {

        private Long userId;

        public Input(Integer page, Integer size, Long userId) {
            super(page, size);
            this.userId = userId;
        }
    }
}
