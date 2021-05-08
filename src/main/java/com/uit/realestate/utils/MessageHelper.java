package com.uit.realestate.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageHelper {

    /**
     * Message source.
     */
    @Autowired
    private MessageSource messageSource;

    /**
     * Get message by code.
     * @param code code
     * @param param parameters
     * @return message
     */
    public String getMessage(String code, Object... param) {
        return messageSource.getMessage(code,
                param, Locale.US);
    }
}
