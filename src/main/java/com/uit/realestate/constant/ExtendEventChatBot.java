package com.uit.realestate.constant;

import lombok.Getter;

public enum ExtendEventChatBot {
    INIT(true, "catchupAll", "extent_webhook_deadline"),
    EXTEND_1(false, "followupevent", "extent_webhook_deadline2"),
    EXTEND_2(false, "followupevent2", "extent_webhook_deadline3"),
    EXTEND_3(false, "followupevent3", null);

    @Getter
    private final boolean initEvent;

    @Getter
    private final String action;

    @Getter
    private final String eventNext;

    ExtendEventChatBot(boolean initEvent, String action, String eventNext) {
        this.initEvent = initEvent;
        this.action = action;
        this.eventNext = eventNext;
    }

    public static ExtendEventChatBot of(String str) {
        for (ExtendEventChatBot item : values()) {
            if (item.getAction().equals(str)) {
                return item;
            }
        }
        return INIT;
    }

    public String generateJsonNextEvent(String param) {
        return "{\n" +
                "    \"followupEventInput\": {\n" +
                "    \"name\": \"" + this.getEventNext() + "\",\n" +
                "    \"parameters\": " + param + ",\n" +
                "    \"languageCode\": \"vi\"\n" +
                "    }\n" +
                "}";
    }
}
