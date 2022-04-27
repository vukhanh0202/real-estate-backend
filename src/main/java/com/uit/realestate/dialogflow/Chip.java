package com.uit.realestate.dialogflow;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Chip {

    private final String type = "chips";

    private List<Option> options;

    public Chip(Map<String, String> options){
        this.options = new ArrayList<>();
        for (String key: options.keySet()) {
            this.options.add(new Option(key, options.get(key)));
        }
    }

    @Data
    class Option{
        private String text;
        private String link;

        public Option(String text, String link) {
            this.text = text;
            this.link = link;
        }
    }
}
