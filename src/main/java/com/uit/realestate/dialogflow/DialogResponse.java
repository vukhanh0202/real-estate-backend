package com.uit.realestate.dialogflow;

import com.uit.realestate.utils.JsonUtils;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.*;

@Data
public class DialogResponse implements Serializable {
    //    private Map<String, Object> payload = new HashMap<>();
//    private Map<String, Object> google = new HashMap<>();
//    private boolean expectUserResponse;
//    private Map<String, Object> richResponse = new HashMap<>();
//    private List<Object> items = new ArrayList<>();
//    private Map<String, String> simpleResponse = new HashMap<>();
    private String text;

    public String convertText() {
        return "{\n" +
                "     \"fulfillmentText\":response\n" +
                "    ,\"fulfillmentMessages\":[\n" +
                "        {\n" +
                "            \"text\": {\n" +
                "                \"text\": [\n" +
                "                    \"" + this.text + "\"\n" +
                "                ]\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "    ,\"source\":\"\"\n" +
                "}";
    }

    public DialogResponse(String text) {
        this.text = text;
    }

}
