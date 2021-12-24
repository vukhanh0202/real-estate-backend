package com.uit.realestate.dto.statistic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uit.realestate.dto.BaseDto;
import com.uit.realestate.dto.apartment.ApartmentBasicDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatisticDto{

    private List<String> labels;

    private List<Long> data;

    private List<ApartmentBasicDto> highLightApartments;

    private TotalStatisticDto totalStatisticDto;
}
