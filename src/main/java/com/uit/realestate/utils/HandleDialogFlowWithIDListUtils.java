package com.uit.realestate.utils;

import com.google.common.collect.ImmutableSet;
import com.uit.realestate.constant.enums.ENumber;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public final class HandleDialogFlowWithIDListUtils {

    private final static Set<String> SPECIAL_REMOVE = ImmutableSet.of("[", "]");

    public static List<Long> execute(String str) {
        str = str.toLowerCase();
        for (String rm : SPECIAL_REMOVE) {
            str = str.replace(rm, "");
        }
        if (Objects.equals(str, "")) {
            return null;
        }
        return List.of(str.split(",")).stream().map(item -> {
            Long id = Long.valueOf((item.split("_")[0]).trim());
            return id;
        }).collect(Collectors.toList());

    }

    private static String generateStrFromENumber() {
        String result = "";
        for (ENumber item : ENumber.values()) {
            if (item.equals(ENumber.EMPTY)) {
                continue;
            }
            for (String str : item.getTexts()) {
                result = result.concat(str.replace(" ", "\\s*").concat("|"));
            }
        }
        return "(" + result + ")";
    }

    private static Double generateDoubleFromString(String rq) {
        rq = rq.replace("-", "");
        Double number = StringUtils.castNumberFromString(rq);
        Pattern pattern = Pattern.compile(generateStrFromENumber());
        List<MatchResult> matchResults = pattern.matcher(rq).results()
                .collect(Collectors.toList()).stream().filter(item -> !item.group(0).equals("")).collect(Collectors.toList());
        if (matchResults.size() == 0) {
            return number;
        }
        return number * ENumber.of(matchResults.stream().findFirst().get().group(0)).getValue();
    }

}
