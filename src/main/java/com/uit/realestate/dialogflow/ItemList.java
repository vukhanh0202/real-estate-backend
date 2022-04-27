package com.uit.realestate.dialogflow;

import lombok.Data;

import java.util.List;

@Data
public class ItemList {

    private final String type = "list";

    private String title;

    private String subtitle;

    private Image image;

    private String link;

    public ItemList(String title, String subtitle, String image, String link) {
        this.title = title;
        this.subtitle = subtitle;
        this.image = new Image(image);
        this.link = link;
    }

    @Data
    class Image{
        private Source src;

        public Image(String src) {
            this.src = new Source(src);
        }

        @Data
        class Source{
            private String rawUrl;

            public Source(String rawUrl) {
                this.rawUrl = rawUrl;
            }
        }
    }
}
