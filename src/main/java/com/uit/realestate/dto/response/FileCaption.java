package com.uit.realestate.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileCaption implements Serializable {

    private static final long serialVersionUID = 1L;
    private String link;
    private String originalName;
    private String name;
    private Boolean isPrivate;
    private String source;
    private String extension;

    public FileCaption(String link) {
        this.link = link;
    }

    public FileCaption(String name, String extension) {
        this.name = name;
        this.extension = extension;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileCaption that = (FileCaption) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
