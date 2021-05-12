package com.uit.realestate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.uit.realestate.dto.user.UserDto;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

public class BaseDto {
    private static final long serialVersionUID = 1L;

    @JsonProperty("is_deleted")
    private Boolean isDeleted;

    @JsonProperty("created_by")
    private UserDto createdBy;

    @JsonProperty("updated_by")
    private UserDto updatedBy;

    @JsonProperty("created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @JsonProperty("updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}
