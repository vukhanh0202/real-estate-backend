package com.uit.realestate.dto.apartment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uit.realestate.dto.response.FileCaption;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApartmentCompareDto{

    private Long id;

    private String title;

    private String address;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("total_price")
    private String totalPrice;

    private Double area;

    @JsonProperty("type_apartment")
    private String typeApartment;

    private List<FileCaption> photos;

    @JsonProperty("bedroom_quantity")
    private Long bedroomQuantity;

    @JsonProperty("bathroom_quantity")
    private Long bathroomQuantity;

    @JsonProperty("floor_quantity")
    private Long floorQuantity;

    @JsonProperty("house_building")
    private String houseDirection;

    @JsonProperty("toilet_quantity")
    private Integer toiletQuantity;

    @JsonProperty("more_info")
    private List<String> moreInfo;

    @JsonProperty("percent_suitable")
    private Double percentSuitable;
}
