package com.uit.realestate.payload.auth;

import lombok.Data;

@Data
public class NewAccountRequest {

    private String username;

    private String password;
}
