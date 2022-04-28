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
                                    generateString() +
                "                ]\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }

    private String generateString(){
        String result = JsonUtils.marshal(itemList);
        if (result == null){
            return "";
        }
        if (chips.isEmpty()){
            result = result.concat(",\n").concat(Objects.requireNonNull(JsonUtils.marshal(chips)));
        }
        return result;
    }

    public String noResponseText(){
        return "\"fulfillmentMessages\": [\n" +
                "      {\n" +
                "        \"text\": {\n" +
                "          \"text\": [\n" +
                "            \"Response configured for matched intent\"\n" +
                "          ]\n" +
                "        }\n" +
                "      }\n" +
                "    ]";
    }

}
