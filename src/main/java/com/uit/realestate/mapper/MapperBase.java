package com.uit.realestate.mapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uit.realestate.domain.user.User;
import com.uit.realestate.dto.response.FileCaption;
import com.uit.realestate.dto.user.UserDto;
import org.apache.commons.text.StringEscapeUtils;
import org.mapstruct.Named;

import java.util.List;

public interface MapperBase {

    @Named("getAudit")
    default UserDto getAudit(User user) {
        if (user == null) return null;
        Gson gson = new Gson();
        return new UserDto(user.getId(), user.getUsername(), gson.fromJson(StringEscapeUtils.unescapeHtml4(user.getAvatar()), FileCaption.class), user.getEmail());
    }

    @Named("getUserInfo")
    default UserDto getUserInfo(User user) {
        if (user == null) return null;
        Gson gson = new Gson();
        return new UserDto(user.getId(), user.getUsername(), user.getFullName(), gson.fromJson(StringEscapeUtils.unescapeHtml4(user.getAvatar()), FileCaption.class), user.getEmail());
    }

    @Named("getFile")
    default FileCaption getPhoto(String file) {
        if (file == null) return null;
        Gson gson = new Gson();
        return gson.fromJson(StringEscapeUtils.unescapeHtml4(file), FileCaption.class);
    }

    @Named("getFiles")
    default List<FileCaption> getFiles(String file) {
        if (file == null) return null;
        Gson gson = new Gson();
        return gson.fromJson(StringEscapeUtils.unescapeHtml4(file), new TypeToken<List<FileCaption>>() {
        }.getType());
    }

    @Named("setFile")
    default String setFile(FileCaption fileCaption) {
        if (fileCaption == null) return null;
        Gson gson = new Gson();
        return gson.toJson(fileCaption);
    }

    @Named("setFiles")
    default String setFiles(List<FileCaption> captions) {
        if (captions == null) return null;
        Gson gson = new Gson();
        return gson.toJson(captions);
    }
}
