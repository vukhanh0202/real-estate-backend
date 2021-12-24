package com.uit.realestate.dto.statistic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uit.realestate.dto.apartment.ApartmentBasicDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TotalStatisticDto {

    @JsonProperty("total_apartment")
    private String totalApartment;

    @JsonProperty("total_price")
    private String totalPrice;

    @JsonProperty("average_price")
    private String averagePrice;

    @JsonProperty("total_square")
    private String totalSquare;

    @JsonProperty("average_square")
    private String averageSquare;

}
