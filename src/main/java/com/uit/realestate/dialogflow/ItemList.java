package com.uit.realestate.dialogflow;

import lombok.Data;

@Data
public class ItemList {

    private final int MAX_LENGTH_STRING = 50;

    private String actionLink;

    private Image image;

    private String subtitle;

    private String title;

    private String type;

    public ItemList(String title, String subtitle, String image, String actionLink) {
        this.title = this.subString(title);
        this.subtitle = subtitle;
        this.image = new Image(image);
        this.actionLink = actionLink;
        this.type = "info";
    }

    public ItemList(String type) {
        this.type = type;
    }

    @Data
    class Image {
        private Source src;

        public Image(String src) {
            this.src = new Source(src);
        }

        @Data
        class Source {
            private String rawUrl;

            public Source(String rawUrl) {
                this.rawUrl = rawUrl;
            }
        }
    }

    private String subString(String str) {
        if (str.length() > MAX_LENGTH_STRING) {
            return str.substring(0, MAX_LENGTH_STRING).concat("...");
        }
        return str;
    }
}
