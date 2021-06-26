package com.uit.realestate.payload.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uit.realestate.dto.response.FileCaption;
import com.uit.realestate.payload.address.UserAddressRequest;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
public class UpdateAvatarUserRequest {

    @JsonIgnore
    private Long id;

    private MultipartFile files;

    @JsonIgnore
    private FileCaption photo;

    public UpdateAvatarUserRequest(Long id, MultipartFile files) {
        this.id = id;
        this.files = files;
    }
}
