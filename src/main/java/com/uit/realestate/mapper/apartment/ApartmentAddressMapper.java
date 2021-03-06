package com.uit.realestate.mapper.apartment;

import com.uit.realestate.domain.apartment.ApartmentAddress;
import com.uit.realestate.dto.apartment.ApartmentAddressDto;
import com.uit.realestate.mapper.MapperBase;
import com.uit.realestate.payload.address.ApartmentAddressRequest;
import com.uit.realestate.repository.location.CountryRepository;
import com.uit.realestate.repository.location.DistrictRepository;
import com.uit.realestate.repository.location.ProvinceRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public abstract class ApartmentAddressMapper implements MapperBase {

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private CountryRepository countryRepository;

    //*************************************************
    //********** Mapper ApartmentAdd To ApartmentDetailDto **********
    //*************************************************
    @Named("toApartmentAddress")
    @BeforeMapping
    protected void toApartmentAddress(ApartmentAddressRequest apartmentAddressRequest,
                                      @MappingTarget ApartmentAddress apartmentAddress) {
        apartmentAddress.setCountry(countryRepository.findById(apartmentAddressRequest.getCountryCode()).get());
        apartmentAddress.setProvince(provinceRepository.findById(apartmentAddressRequest.getProvinceId()).get());
        apartmentAddress.setDistrict(districtRepository.findById(apartmentAddressRequest.getDistrictId()).get());
    }

    @BeanMapping(qualifiedByName = "toApartmentAddress", ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "address", target = "address")
    public abstract ApartmentAddress toApartmentAddress(ApartmentAddressRequest apartmentDetail);


    @Named("toApartmentAddressMapping")
    @BeforeMapping
    protected void toApartmentAddressMapping(ApartmentAddressRequest apartmentAddressRequest,
                                             @MappingTarget ApartmentAddress apartmentAddress) {
        if (apartmentAddressRequest.getCountryCode() != null) {
            apartmentAddress.setCountry(countryRepository.findById(apartmentAddressRequest.getCountryCode()).get());
        }
        if (apartmentAddressRequest.getProvinceId() != null) {
            apartmentAddress.setProvince(provinceRepository.findById(apartmentAddressRequest.getProvinceId()).get());
        }
        if (apartmentAddressRequest.getDistrictId() != null) {
            apartmentAddress.setDistrict(districtRepository.findById(apartmentAddressRequest.getDistrictId()).get());
        }
    }

    @BeanMapping(qualifiedByName = "toApartmentAddressMapping", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "address", target = "address")
    public abstract void updateApartmentAddress(ApartmentAddressRequest dto, @MappingTarget ApartmentAddress apartment);

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "province.id", target = "provinceId")
    @Mapping(source = "province.name", target = "provinceName")
    @Mapping(source = "country.code", target = "countryCode")
    @Mapping(source = "country.name", target = "countryName")
    @Mapping(source = "district.id", target = "districtId")
    @Mapping(source = "district.name", target = "districtName")
    @Mapping(source = "address", target = "address")
    public abstract ApartmentAddressDto toApartmentAddressDto(ApartmentAddress apartmentAddress);

}
