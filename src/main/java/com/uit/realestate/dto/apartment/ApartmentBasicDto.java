package com.uit.realestate.dto.apartment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.dto.BaseDto;
import com.uit.realestate.dto.response.FileCaption;
import com.uit.realestate.dto.user.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

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

    private EApartmentStatus status;

    private List<FileCaption> photos;

    private UserDto author;

    @JsonProperty("percent_suitable")
    private Double percentSuitable;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ApartmentBasicDto that = (ApartmentBasicDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
