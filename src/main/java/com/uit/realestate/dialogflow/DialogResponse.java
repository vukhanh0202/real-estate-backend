package com.uit.realestate.dialogflow;

import com.uit.realestate.utils.JsonUtils;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.*;

@Data
public class DialogResponse implements Serializable {
    private List<ItemList> itemList;
    private List<Chip> chips;

    public String convertText() {
        return "{\n" +
                "    \"fulfillmentMessages\": [\n" +
                "        {\n" +
                "            \"payload\": {\n" +
                "                \"richContent\": [\n" +
                                    JsonUtils.marshal(itemList) + ",\n"+
                                    JsonUtils.marshal(chips) + "\n"+
                "                ]\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }

}
