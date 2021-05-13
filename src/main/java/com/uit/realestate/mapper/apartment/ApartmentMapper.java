package com.uit.realestate.mapper.apartment;

import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.domain.apartment.Category;
import com.uit.realestate.dto.apartment.ApartmentBasicDto;
import com.uit.realestate.dto.category.CategoryDto;
import com.uit.realestate.mapper.MapperBase;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public abstract class ApartmentMapper implements MapperBase {

    //*************************************************
    //********** Mapper Apartment To ApartmentBasicDto (Search) **********
    //*************************************************
    @Named("toApartmentBasicDto")
    @BeforeMapping
    protected void toApartmentBasicDto(Apartment apartment, @MappingTarget ApartmentBasicDto dto) {
        dto.setAddress(apartment.getHouseAddress().getDistrictName() + ", " + apartment.getHouseAddress().getProvinceName());
    }

    @BeanMapping(qualifiedByName = "toApartmentBasicDto", ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "code", target = "code")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "overview", target = "overview")
    @Mapping(source = "totalPrice", target = "totalPrice")
    @Mapping(source = "area", target = "area")
    public abstract ApartmentBasicDto toApartmentBasicDto(Apartment apartment);

    @BeanMapping(ignoreByDefault = true)
    public abstract List<ApartmentBasicDto> toApartmentBasicDtoList(List<Apartment> apartmentList);
}
