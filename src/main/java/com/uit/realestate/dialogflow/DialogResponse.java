package com.uit.realestate.dialogflow;

import com.uit.realestate.utils.JsonUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.*;

@Data
public class DialogResponse implements Serializable {
    private List<ItemList> itemList;
    private List<Chip> chips;
    private List<String> texts = new ArrayList<>();

    public String convertText() {
        return "{\n" +
                "    \"fulfillmentMessages\": [\n" +
                "       {\n" +
                "           \"text\": {\n" +
                "               \"text\": " +
                JsonUtils.marshal(texts) +
                "           }\n" +
                "        },\n" +
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

    private String generateString() {
        String result = JsonUtils.marshal(this.insertDivider(itemList));
        if (result == null) {
            return "";
        }
        if (chips != null && !chips.isEmpty()) {
            result = result.concat(",\n").concat(Objects.requireNonNull(JsonUtils.marshal(chips)));
        }
        return result;
    }

    private List<ItemList> insertDivider(List<ItemList> list) {
        List<ItemList> result = new ArrayList<>();
        for (ItemList item : list) {
            ItemList divider = new ItemList("divider");
            result.add(item);
            result.add(divider);
        }
        return result;
    }

    public String noResponseText() {
        return "{\"fulfillmentMessages\": [\n" +
                "      {\n" +
                "        \"text\": {\n" +
                "          \"text\": [\n" +
                "            \"Rất tiếc hệ thống chưa tìm thấy sản phẩm phù hợp với bạn. Thử lại nhé!\"\n" +
                "          ]\n" +
                "        }\n" +
                "      }\n" +
                "    ]}";
    }

    public DialogResponse addChips(Map<String, String> maps) {
        List<Chip> result = new ArrayList<>();
        for (String key : maps.keySet()) {
            result.add(new Chip(Collections.singletonMap(key, maps.get(key))));
        }
        this.setChips(result);
        return this;
    }

}
