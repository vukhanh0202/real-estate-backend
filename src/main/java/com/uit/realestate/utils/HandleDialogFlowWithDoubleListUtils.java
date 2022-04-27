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
public final class HandleDialogFlowWithDoubleListUtils {

    private final static Set<String> SPECIAL_REMOVE = ImmutableSet.of("m2", "mét2", "met2");

    public static List<Double> execute(String str) {
        if (Objects.equals(str, "")){
            return null;
        }
        str = str.toLowerCase();
        for (String rm : SPECIAL_REMOVE) {
            str = str.replace(rm, "");
        }
        // Catch unit if only 1 unit in range (ex: 2-3 tỷ)
        Double unit;
        Pattern patternUnit = Pattern.compile(generateStrFromENumber());
        List<MatchResult> matchResults = patternUnit.matcher(str).results()
                .collect(Collectors.toList()).stream().filter(item -> !item.group(0).equals("")).collect(Collectors.toList());
        if (matchResults.size() == 1) {
            unit = ENumber.of(matchResults.stream().findFirst().get().group(0)).getValue();
        }else {
            unit = -1D;
        }


        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?(\\,\\d+)?(\\s*)" + generateStrFromENumber());
        return pattern.matcher(str).results()
                .collect(Collectors.toList())
                .stream()
                .map(matchResult -> generateDoubleFromString(matchResult.group(0).replace(",", "").replace(".", ""), unit))
                .collect(Collectors.toList())
                .stream().sorted(Double::compareTo)
                .collect(Collectors.toList());
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

    private static Double generateDoubleFromString(String rq, Double unit) {
        rq = rq.replace("-", "");
        Double number = StringUtils.castNumberFromString(rq);
        if (unit != -1D){
            return number * unit;
        }
        Pattern pattern = Pattern.compile(generateStrFromENumber());
        List<MatchResult> matchResults = pattern.matcher(rq).results()
                .collect(Collectors.toList()).stream().filter(item -> !item.group(0).equals("")).collect(Collectors.toList());
        if (matchResults.size() == 0) {
            return number;
        }
        return number * ENumber.of(matchResults.stream().findFirst().get().group(0)).getValue();
    }

}
