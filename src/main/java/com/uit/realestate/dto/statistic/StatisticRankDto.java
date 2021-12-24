package com.uit.realestate.dto.statistic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatisticRankDto {

    @JsonProperty("total_posts")
    private List<String> totalPosts;

    @JsonProperty("total_prices")
    private List<String> totalPrices;

    @JsonProperty("total_areas")
    private List<String> totalAreas;

}
