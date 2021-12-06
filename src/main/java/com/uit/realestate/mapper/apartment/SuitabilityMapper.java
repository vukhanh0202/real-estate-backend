package com.uit.realestate.mapper.apartment;

import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.dto.SuitabilityDto;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public abstract class SuitabilityMapper {

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "apartmentAddress.province.id", target = "provinceId")
    @Mapping(source = "apartmentAddress.district.id", target = "districtId")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "totalPrice", target = "totalPrice")
    @Mapping(source = "area", target = "area")
    @Mapping(source = "apartmentDetail.bedroomQuantity", target = "bedroomQuantity")
    @Mapping(source = "apartmentDetail.bathroomQuantity", target = "bathroomQuantity")
    @Mapping(source = "apartmentDetail.floorQuantity", target = "floorQuantity")
    public abstract SuitabilityDto toSuitabilityDto(Apartment apartment);
}
