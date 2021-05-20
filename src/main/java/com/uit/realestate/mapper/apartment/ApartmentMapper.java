package com.uit.realestate.mapper.apartment;

import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.dto.apartment.ApartmentDto;
import com.uit.realestate.mapper.MapperBase;
import com.uit.realestate.payload.apartment.AddApartmentRequest;
import com.uit.realestate.repository.category.CategoryRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public abstract class ApartmentMapper implements MapperBase {

    @Autowired
    private ApartmentDetailMapper apartmentDetailMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ApartmentAddressMapper apartmentAddressMapper;

    //*************************************************
    //********** Mapper Apartment To ApartmentBasicDto (Search) **********
    //*************************************************
    @Named("toApartmentBasicDto")
    @BeforeMapping
    protected void toApartmentBasicDto(Apartment apartment, @MappingTarget ApartmentDto dto) {
        if (apartment.getApartmentAddress()!=null){
            dto.setAddress(apartment.getApartmentAddress().getDistrict().getName()
                    + ", " + apartment.getApartmentAddress().getProvince().getName());
        }
    }

    @BeanMapping(qualifiedByName = "toApartmentBasicDto", ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Named("toApartmentBasicDtoList")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "overview", target = "overview")
    @Mapping(source = "totalPrice", target = "totalPrice")
    @Mapping(source = "area", target = "area")
    public abstract ApartmentDto toApartmentBasicDto(Apartment apartment);

    @BeanMapping(ignoreByDefault = true)
    @IterableMapping(qualifiedByName = "toApartmentBasicDtoList")
    public abstract List<ApartmentDto> toApartmentBasicDtoList(List<Apartment> apartmentList);



    //*************************************************
    //********** Mapper Apartment To ApartmentFullDto (Detail) **********
    //*************************************************
    @Named("toApartmentFullDto")
    @BeforeMapping
    protected void toApartmentFullDto(Apartment apartment, @MappingTarget ApartmentDto dto) {
        dto.setAddress(apartment.getApartmentAddress().getDistrict().getName()
                + ", " + apartment.getApartmentAddress().getProvince().getName());
        dto.setApartmentDetail(apartmentDetailMapper.toApartmentDetailDto(apartment.getApartmentDetail()));
    }

    @BeanMapping(qualifiedByName = "toApartmentFullDto", ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "overview", target = "overview")
    @Mapping(source = "totalPrice", target = "totalPrice")
    @Mapping(source = "area", target = "area")
    public abstract ApartmentDto toApartmentFullDto(Apartment apartment);

    //*************************************************
    //********** Mapper AddApartmentRequest To Apartment (Entity) **********
    //*************************************************
    @Named("toApartment")
    @BeforeMapping
    protected void toApartment(AddApartmentRequest addApartmentRequest, @MappingTarget Apartment apartment) {
        apartment.setCategory(categoryRepository.findById(addApartmentRequest.getCategoryId()).get());
        apartment.setApartmentAddress(apartmentAddressMapper.toApartmentAddress(addApartmentRequest.getApartmentAddress()));
        apartment.setApartmentDetail(apartmentDetailMapper.toApartmentDetail(addApartmentRequest.getApartmentDetail()));
    }

    @BeanMapping(qualifiedByName = "toApartment", ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "title", target = "title")
    @Mapping(source = "overview", target = "overview")
    @Mapping(source = "area", target = "area")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "totalPrice", target = "totalPrice")
    @Mapping(source = "typeApartment", target = "typeApartment")
    @Mapping(source = "expiredDate", target = "expiredDate")
    public abstract Apartment toApartment(AddApartmentRequest addApartmentRequest);

}
