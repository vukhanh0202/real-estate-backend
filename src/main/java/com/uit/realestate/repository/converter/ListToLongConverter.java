package com.uit.realestate.repository.converter;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ListToLongConverter implements AttributeConverter<List<Long>, String> {
    @Override
    public String convertToDatabaseColumn(List<Long> attribute) {
        return attribute == null ? null : attribute.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    @Override
    public List<Long> convertToEntityAttribute(String dbData) {
        return dbData == null || dbData.equals("") ? Collections.emptyList() : Arrays.stream(dbData.split(",")).map(Long::valueOf).collect(Collectors.toList());
    }
}