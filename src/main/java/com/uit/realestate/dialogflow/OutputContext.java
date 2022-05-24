package com.uit.realestate.dialogflow;

import lombok.Data;

import java.util.Map;

@Data
public class OutputContext {
    private String name;
    private Map<String, Object> parameters;
}
