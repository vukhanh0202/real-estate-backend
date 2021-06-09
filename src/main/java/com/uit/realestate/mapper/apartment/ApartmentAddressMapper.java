package com.uit.realestate.mapper.apartment;

import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.domain.apartment.ApartmentAddress;
import com.uit.realestate.domain.apartment.ApartmentDetail;
import com.uit.realestate.dto.apartment.ApartmentDetailDto;
import com.uit.realestate.mapper.MapperBase;
import com.uit.realestate.mapper.location.CountryMapper;
import com.uit.realestate.mapper.location.DistrictMapper;
import com.uit.realestate.mapper.location.ProvinceMapper;
import com.uit.realestate.payload.address.ApartmentAddressRequest;
import com.uit.realestate.payload.apartment.AddApartmentRequest;
import com.uit.realestate.payload.apartment.UpdateApartmentRequest;
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

}
