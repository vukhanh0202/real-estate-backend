package com.uit.realestate.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@Slf4j
public final class HandleDialogFlowWithSingleStringUtils {

    public static List<String> execute(String str) {
        if (Objects.equals(str, "")){
            return null;
        }
        return List.of(str);
    }
}
