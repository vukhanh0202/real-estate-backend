package com.uit.realestate.mapper.apartment;

import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.dto.apartment.ApartmentBasicDto;
import com.uit.realestate.dto.apartment.ApartmentCompareDto;
import com.uit.realestate.dto.apartment.ApartmentDto;
import com.uit.realestate.mapper.MapperBase;
import com.uit.realestate.payload.apartment.AddApartmentRequest;
import com.uit.realestate.payload.apartment.UpdateApartmentRequest;
import com.uit.realestate.repository.action.FavouriteRepository;
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

    @Autowired
    private FavouriteRepository favouriteRepository;

    //*************************************************
    //********** Mapper Apartment To ApartmentBasicDto (Search) **********
    //*************************************************
    @Named("toApartmentPreviewDto")
    @BeforeMapping
    protected void toApartmentPreviewDto(Apartment apartment, @MappingTarget ApartmentDto dto, @Context Long userId) {
        if (apartment.getApartmentAddress() != null) {
            dto.setAddress(apartment.getApartmentAddress().getDistrict().getName()
                    + ", " + apartment.getApartmentAddress().getProvince().getName());
            dto.setAddressDetail(apartmentAddressMapper.toApartmentAddressDto(apartment.getApartmentAddress()));
        }
        if (userId != null && favouriteRepository.findByApartmentIdAndUserId(apartment.getId(), userId).isPresent()) {
            dto.setFavourite(true);
        }
        dto.setTypeApartment(apartment.getTypeApartment().getValue());

    }

    @BeanMapping(qualifiedByName = "toApartmentPreviewDto", ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Named("toApartmentPreviewDtoList")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "overview", target = "overview")
    @Mapping(source = "totalPrice", target = "totalPrice")
    @Mapping(source = "area", target = "area")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "apartmentDetail.bedroomQuantity", target = "bedroomQuantity")
    @Mapping(source = "apartmentDetail.bathroomQuantity", target = "bathroomQuantity")
    @Mapping(source = "apartmentDetail.floorQuantity", target = "floorQuantity")
    @Mapping(source = "photos", target = "photos", qualifiedByName = "getFiles")
    @Mapping(source = "highlight", target = "isHighlight")
    @Mapping(source = "author", target = "author", qualifiedByName = "getUserInfo")
    public abstract ApartmentDto toApartmentPreviewDto(Apartment apartment, @Context Long userId);

    @BeanMapping(ignoreByDefault = true)
    @IterableMapping(qualifiedByName = "toApartmentPreviewDtoList")
    public abstract List<ApartmentDto> toApartmentPreviewDtoList(List<Apartment> apartmentList, @Context Long userId);


    //*************************************************
    //********** Mapper Apartment To ApartmentFullDto (Detail) **********
    //*************************************************
    @Named("toApartmentFullDto")
    @BeforeMapping
    protected void toApartmentFullDto(Apartment apartment, @MappingTarget ApartmentDto dto, @Context Long userId) {
        dto.setAddress(apartment.getApartmentAddress().getDistrict().getName()
                + ", " + apartment.getApartmentAddress().getProvince().getName());
        dto.setApartmentDetail(apartmentDetailMapper.toApartmentDetailDto(apartment.getApartmentDetail()));
        dto.setAddressDetail(apartmentAddressMapper.toApartmentAddressDto(apartment.getApartmentAddress()));
        if (userId != null && favouriteRepository.findByApartmentIdAndUserId(apartment.getId(), userId).isPresent()) {
            dto.setFavourite(true);
        }
        dto.setTypeApartment(apartment.getTypeApartment().getValue());
    }

    @BeanMapping(qualifiedByName = "toApartmentFullDto", ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "overview", target = "overview")
    @Mapping(source = "totalPrice", target = "totalPrice")
    @Mapping(source = "area", target = "area")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "photos", target = "photos", qualifiedByName = "getFiles")
    @Mapping(source = "author", target = "author", qualifiedByName = "getUserInfo")
    public abstract ApartmentDto toApartmentFullDto(Apartment apartment, @Context Long userId);


    //*************************************************
    //********** Mapper Apartment To Apartment Basic **********
    //*************************************************
    @Named("toApartmentBasicDto")
    @BeforeMapping
    protected void toApartmentBasicDto(Apartment apartment, @MappingTarget ApartmentBasicDto dto, @Context Long userId) {
        dto.setAddress(apartment.getApartmentAddress().getDistrict().getName()
                + ", " + apartment.getApartmentAddress().getProvince().getName());
        if (userId != null && favouriteRepository.findByApartmentIdAndUserId(apartment.getId(), userId).isPresent()) {
            dto.setFavourite(true);
        }
        dto.setTypeApartment(apartment.getTypeApartment().getValue());
    }

    @BeanMapping(qualifiedByName = "toApartmentBasicDto", ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "overview", target = "overview")
    @Mapping(source = "totalPrice", target = "totalPrice")
    @Mapping(source = "apartmentDetail.bedroomQuantity", target = "bedroomQuantity")
    @Mapping(source = "apartmentDetail.bathroomQuantity", target = "bathroomQuantity")
    @Mapping(source = "area", target = "area")
    @Mapping(source = "createdBy", target = "createdBy", qualifiedByName = "getAudit")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "photos", target = "photos", qualifiedByName = "getFiles")
    @Mapping(source = "author", target = "author", qualifiedByName = "getUserInfo")
    public abstract ApartmentBasicDto toApartmentBasicDto(Apartment apartment, @Context Long userId);

    @BeanMapping(ignoreByDefault = true)
    public abstract List<ApartmentBasicDto> toApartmentBasicDtoList(List<Apartment> apartmentList, @Context Long userId);

    //*************************************************
    //********** Mapper AddApartmentRequest To Apartment (Entity) **********
    //*************************************************
    @Named("toApartment")
    @BeforeMapping
    protected void toApartment(AddApartmentRequest addApartmentRequest, @MappingTarget Apartment apartment) {
        apartment.setCategory(categoryRepository.findById(addApartmentRequest.getCategoryId()).get());
        apartment.setApartmentAddress(apartmentAddressMapper.toApartmentAddress(addApartmentRequest.getApartmentAddress()));
        apartment.setApartmentDetail(apartmentDetailMapper.toApartmentDetail(addApartmentRequest.getApartmentDetail()));
        apartment.setPrice((double) Math.round(addApartmentRequest.getTotalPrice() / addApartmentRequest.getArea()));

    }

    @BeanMapping(qualifiedByName = "toApartment", ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "title", target = "title")
    @Mapping(source = "overview", target = "overview")
    @Mapping(source = "area", target = "area")
    @Mapping(source = "totalPrice", target = "totalPrice")
    @Mapping(source = "typeApartment", target = "typeApartment")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "authorId", target = "author.id")
    @Mapping(source = "photos", target = "photos", qualifiedByName = "setFiles")
    public abstract Apartment toApartment(AddApartmentRequest addApartmentRequest);

    @Named("updateApartmentMapping")
    @BeforeMapping
    protected void updateApartmentMapping(UpdateApartmentRequest updateApartmentRequest, @MappingTarget Apartment apartment) {
        if (updateApartmentRequest.getCategoryId() != null) {
            apartment.setCategory(categoryRepository.findById(updateApartmentRequest.getCategoryId()).get());
        }
        if (updateApartmentRequest.getApartmentAddress() != null) {
            apartmentAddressMapper.updateApartmentAddress(updateApartmentRequest.getApartmentAddress(), apartment.getApartmentAddress());
        }
        if (updateApartmentRequest.getApartmentDetail() != null) {
            apartmentDetailMapper.updateApartmentDetail(updateApartmentRequest.getApartmentDetail(), apartment.getApartmentDetail());
        }
        if (updateApartmentRequest.getTotalPrice() != null || updateApartmentRequest.getArea() != null)
            apartment.setPrice((double) Math.round(updateApartmentRequest.getTotalPrice() / updateApartmentRequest.getArea()));
    }

    @BeanMapping(qualifiedByName = "updateApartmentMapping", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "title", target = "title")
    @Mapping(source = "overview", target = "overview")
    @Mapping(source = "area", target = "area")
    @Mapping(source = "totalPrice", target = "totalPrice")
    @Mapping(source = "typeApartment", target = "typeApartment")
    @Mapping(source = "photos", target = "photos", qualifiedByName = "setFiles")
    public abstract void updateApartment(UpdateApartmentRequest dto, @MappingTarget Apartment apartment);


    //*************************************************
    //********** Mapper AddApartmentRequest To Apartment (Entity) **********
    //*************************************************
    @Named("toApartmentCompareDto")
    @BeforeMapping
    protected void toApartmentCompareDto(Apartment apartment, @MappingTarget ApartmentCompareDto apartmentCompareDto) {
        apartmentCompareDto.setAddress(apartment.getApartmentAddress().getAddress() + ", "
                + apartment.getApartmentAddress().getDistrict().getName() + ", "
                + apartment.getApartmentAddress().getProvince().getName() + ", "
                + apartment.getApartmentAddress().getCountry().getName());

    }
    @BeanMapping(qualifiedByName = "toApartmentCompareDto", ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "totalPrice", target = "totalPrice")
    @Mapping(source = "area", target = "area")
    @Mapping(source = "typeApartment", target = "typeApartment")
    @Mapping(source = "apartmentDetail.bedroomQuantity", target = "bedroomQuantity")
    @Mapping(source = "apartmentDetail.bathroomQuantity", target = "bathroomQuantity")
    @Mapping(source = "apartmentDetail.floorQuantity", target = "floorQuantity")
    @Mapping(source = "apartmentDetail.frontBuilding", target = "frontBuilding")
    @Mapping(source = "apartmentDetail.houseDirection", target = "houseDirection")
    @Mapping(source = "apartmentDetail.balconyDirection", target = "balconyDirection")
    @Mapping(source = "apartmentDetail.toiletQuantity", target = "toiletQuantity")
    @Mapping(source = "apartmentDetail.furniture", target = "furniture")
    @Mapping(source = "photos", target = "photos", qualifiedByName = "getFiles")
    public abstract ApartmentCompareDto toApartmentCompareDto(Apartment apartment);

    @BeanMapping(ignoreByDefault = true)
    public abstract List<ApartmentCompareDto> toApartmentCompareDtoList(List<Apartment> apartmentList);

}
