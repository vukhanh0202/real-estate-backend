package com.uit.realestate.payload.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateUserRequest {

    @JsonIgnore
    private Long id;

    @JsonProperty("full_name")
    private String fullName;

    private String email;

    private String phone;

    private String description;
}
