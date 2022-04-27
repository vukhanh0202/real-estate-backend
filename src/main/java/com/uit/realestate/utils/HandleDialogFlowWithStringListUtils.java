package com.uit.realestate.utils;

import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public final class HandleDialogFlowWithStringListUtils {

    private final static Set<String> SPECIAL_REMOVE = ImmutableSet.of("[", "]");

    public static List<String> execute(String str) {
        str = str.toLowerCase();
        for (String rm : SPECIAL_REMOVE) {
            str = str.replace(rm, "");
        }
        if (Objects.equals(str, "")){
            return null;
        }
        return List.of(str.split(",")).stream().map(String::trim).collect(Collectors.toList());
    }
}
