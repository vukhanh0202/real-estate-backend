package com.uit.realestate.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uit.realestate.dto.BaseDto;
import com.uit.realestate.dto.address.UserAddressDto;
import com.uit.realestate.dto.response.FileCaption;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto extends BaseDto {

    private Long id;

    private String username;

    @JsonProperty("full_name")
    private String fullName;

    private String email;

    private String phone;

    private FileCaption avatar;

    private String description;

    private UserAddressDto addressDto;

    public UserDto(Long id, String username, FileCaption avatar, String email) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
        this.email = email;
    }

    public UserDto(Long id, String username,String fullName, FileCaption avatar, String email) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.avatar = avatar;
        this.email = email;
    }
}
