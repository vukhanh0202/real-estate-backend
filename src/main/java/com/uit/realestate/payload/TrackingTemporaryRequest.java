package com.uit.realestate.payload;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@Builder
public class TrackingTemporaryRequest {

    @NonNull
    private String key;
    private List<Long> listRecommendWithUserTargets;
    private List<Long> listRecommendWithUserOrIp;
    private List<Long> listRecommendWithLatestRandom;
}
