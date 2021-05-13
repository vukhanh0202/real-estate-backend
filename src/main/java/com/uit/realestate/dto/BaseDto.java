package com.uit.realestate.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uit.realestate.dto.user.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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
