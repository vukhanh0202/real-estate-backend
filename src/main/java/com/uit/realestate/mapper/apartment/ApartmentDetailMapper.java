package com.uit.realestate.mapper.apartment;

import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.domain.apartment.ApartmentAddress;
import com.uit.realestate.domain.apartment.ApartmentDetail;
import com.uit.realestate.dto.apartment.ApartmentDetailDto;
import com.uit.realestate.dto.apartment.ApartmentDto;
import com.uit.realestate.mapper.MapperBase;
import com.uit.realestate.payload.address.ApartmentAddressRequest;
import com.uit.realestate.payload.apartment.ApartmentDetailRequest;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public abstract class ApartmentDetailMapper implements MapperBase {

    //*************************************************
    //********** Mapper ApartmentDetail To ApartmentDetailDto **********
    //*************************************************

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "description", target = "description")
    @Mapping(source = "frontBuilding", target = "frontBuilding")
    @Mapping(source = "entranceBuilding", target = "entranceBuilding")
    @Mapping(source = "houseDirection", target = "houseDirection")
    @Mapping(source = "balconyDirection", target = "balconyDirection")
    @Mapping(source = "floorQuantity", target = "floorQuantity")
    @Mapping(source = "bedroomQuantity", target = "bedroomQuantity")
    @Mapping(source = "bathroomQuantity", target = "bathroomQuantity")
    @Mapping(source = "toiletQuantity", target = "toiletQuantity")
    @Mapping(source = "furniture", target = "furniture")
    public abstract ApartmentDetailDto toApartmentDetailDto(ApartmentDetail apartmentDetail);


    //*************************************************
    //********** Mapper ApartmentDetailRequest To ApartmentDetail **********
    //*************************************************

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "description", target = "description")
    @Mapping(source = "frontBuilding", target = "frontBuilding")
    @Mapping(source = "entranceBuilding", target = "entranceBuilding")
    @Mapping(source = "houseDirection", target = "houseDirection")
    @Mapping(source = "balconyDirection", target = "balconyDirection")
    @Mapping(source = "floorQuantity", target = "floorQuantity")
    @Mapping(source = "bedroomQuantity", target = "bedroomQuantity")
    @Mapping(source = "bathroomQuantity", target = "bathroomQuantity")
    @Mapping(source = "toiletQuantity", target = "toiletQuantity")
    @Mapping(source = "furniture", target = "furniture")
    public abstract ApartmentDetail toApartmentDetail(ApartmentDetailRequest apartmentDetailRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateApartmentDetail(ApartmentDetailRequest apartmentDetailRequest, @MappingTarget ApartmentDetail apartmentDetail);

}
