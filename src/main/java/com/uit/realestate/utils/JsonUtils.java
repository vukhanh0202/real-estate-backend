package com.uit.realestate.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.uit.realestate.exception.InvalidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class JsonUtils {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        objectMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static <T> List<T> jsonArrayToList(String json, Class<T> clazz){
        try{
            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
            List<T> ts = objectMapper.readValue(json, listType);
            return ts;
        } catch (Exception e){
            log.error("Error when parse json to list" + e.getMessage());
            throw new InvalidException(e.getMessage());
        }
    }

    public static <T> T unmarshal(String data, Class<T> clazz){
        try{
            return objectMapper.readValue(data, clazz);
        } catch (Exception e){
            log.error("Error when unmarshal" + e.getMessage());
            throw new InvalidException(e.getMessage());
        }
    }

    @Nullable
    public static String marshal(@Nullable Object obj){
        if (obj == null){
            return null;
        }
        try{
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e){
            log.error("Error when marshal" + e.getMessage());
            throw new InvalidException(e.getMessage());
        }
    }

    public static <T> T castStringToValue(String data, Class<T> clazz){
        try {
            return clazz.cast(data);
        } catch(ClassCastException e) {
            log.error("Error when cast to " + clazz + " : " + e.getMessage());
            throw new InvalidException(e.getMessage());
        }
    }
}
