package com.uit.realestate.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class ApiResponse {

    /**
     * Is success.
     */
    @Getter
    @Setter
    private boolean success;

    /**
     * Message response.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Getter
    @Setter
    private String message;

    /**
     * Data response.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Getter
    @Setter
    private Object data;

    public ApiResponse(Object data) {
        this(null, data);
    }

    public ApiResponse(boolean success, String message) {
        this(success, message, null);
    }


    public ApiResponse(String message, Object data) {
        this(true, message, data);
    }

    public ApiResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
        if (message != null){
            if (success) {
                log.info(message);
            } else {
                log.error(message);
            }
        }
    }

}
