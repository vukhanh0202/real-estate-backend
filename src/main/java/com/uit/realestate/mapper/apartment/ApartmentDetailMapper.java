package com.uit.realestate.mapper.apartment;

import com.uit.realestate.domain.apartment.ApartmentDetail;
import com.uit.realestate.dto.apartment.ApartmentDetailDto;
import com.uit.realestate.mapper.MapperBase;
import com.uit.realestate.payload.apartment.ApartmentDetailRequest;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public abstract class ApartmentDetailMapper implements MapperBase {

    //*************************************************
    //********** Mapper ApartmentDetail To ApartmentDetailDto **********
    //*************************************************

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "description", target = "description")
    @Mapping(source = "houseDirection", target = "houseDirection")
    @Mapping(source = "floorQuantity", target = "floorQuantity")
    @Mapping(source = "bedroomQuantity", target = "bedroomQuantity")
    @Mapping(source = "bathroomQuantity", target = "bathroomQuantity")
    @Mapping(source = "toiletQuantity", target = "toiletQuantity")
    @Mapping(source = "moreInfo", target = "moreInfo")
    public abstract ApartmentDetailDto toApartmentDetailDto(ApartmentDetail apartmentDetail);


    //*************************************************
    //********** Mapper ApartmentDetailRequest To ApartmentDetail **********
    //*************************************************

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "description", target = "description")
    @Mapping(source = "houseDirection", target = "houseDirection")
    @Mapping(source = "floorQuantity", target = "floorQuantity")
    @Mapping(source = "bedroomQuantity", target = "bedroomQuantity")
    @Mapping(source = "bathroomQuantity", target = "bathroomQuantity")
    @Mapping(source = "toiletQuantity", target = "toiletQuantity")
    @Mapping(source = "moreInfo", target = "moreInfo")
    public abstract ApartmentDetail toApartmentDetail(ApartmentDetailRequest apartmentDetailRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateApartmentDetail(ApartmentDetailRequest apartmentDetailRequest, @MappingTarget ApartmentDetail apartmentDetail);

}
