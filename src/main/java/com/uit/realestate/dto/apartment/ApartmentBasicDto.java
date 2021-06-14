package com.uit.realestate.dto.apartment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uit.realestate.dto.BaseDto;
import com.uit.realestate.dto.response.FileCaption;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApartmentBasicDto extends BaseDto {

    private Long id;

    private String title;

    private String overview;

    private String address;

    @JsonProperty("total_price")
    private Double totalPrice;

    private Double area;

    @JsonProperty("bedroom_quantity")
    private Long bedroomQuantity;

    @JsonProperty("bathroom_quantity")
    private Long bathroomQuantity;

    private FileCaption picture;

    @JsonProperty("category_name")
    private String categoryName;

    private Boolean favourite = false;

    @JsonProperty("type_apartment")
    private String typeApartment;

}
