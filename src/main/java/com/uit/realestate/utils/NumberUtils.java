package com.uit.realestate.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class NumberUtils {

    public static int random(int min, int max) {
        return (int) (Math.floor(Math.random() * (max - min + 1) + min));
    }

    public static int random(double minD, double maxD) {
        int min = (int) minD;
        int max = (int) maxD;
        return (int) (Math.floor(Math.random() * (max - min + 1) + min));
    }
}
