package com.uit.realestate.mapper.address;

import com.uit.realestate.domain.apartment.ApartmentAddress;
import com.uit.realestate.domain.user.User;
import com.uit.realestate.domain.user.UserAddress;
import com.uit.realestate.dto.address.UserAddressDto;
import com.uit.realestate.dto.user.UserDto;
import com.uit.realestate.mapper.MapperBase;
import com.uit.realestate.payload.address.ApartmentAddressRequest;
import com.uit.realestate.payload.address.UserAddressRequest;
import com.uit.realestate.payload.user.UpdateUserRequest;
import com.uit.realestate.repository.location.CountryRepository;
import com.uit.realestate.repository.location.DistrictRepository;
import com.uit.realestate.repository.location.ProvinceRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public abstract class UserAddressMapper implements MapperBase {

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private CountryRepository countryRepository;

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "province.id", target = "provinceId")
    @Mapping(source = "province.name", target = "provinceName")
    @Mapping(source = "country.code", target = "countryCode")
    @Mapping(source = "country.name", target = "countryName")
    @Mapping(source = "district.id", target = "districtId")
    @Mapping(source = "district.name", target = "districtName")
    @Mapping(source = "address", target = "address")
    public abstract UserAddressDto toUserAddressDto(UserAddress userAddress);

    @Named("toUserAddress")
    @BeforeMapping
    protected void toUserAddress(UserAddressRequest userAddressRequest,
                                 @MappingTarget UserAddress userAddress) {
        userAddress.setCountry(countryRepository.findById(userAddressRequest.getCountryCode()).get());
        userAddress.setProvince(provinceRepository.findById(userAddressRequest.getProvinceId()).get());
        userAddress.setDistrict(districtRepository.findById(userAddressRequest.getDistrictId()).get());
    }

    @BeanMapping(qualifiedByName = "toUserAddress", ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "address", target = "address")
    public abstract UserAddress toUserAddress(UserAddressRequest userAddressRequest);

    @Named("updateAddressExist")
    @BeforeMapping
    protected void updateAddressExist(UserAddressRequest userAddressRequest,
                                      @MappingTarget UserAddress userAddress) {
        if (userAddressRequest.getCountryCode() != null) {
            userAddress.setCountry(countryRepository.findById(userAddressRequest.getCountryCode()).get());
        }
        if (userAddressRequest.getProvinceId() != null) {
            userAddress.setProvince(provinceRepository.findById(userAddressRequest.getProvinceId()).get());
        }
        if (userAddressRequest.getDistrictId() != null) {
            userAddress.setDistrict(districtRepository.findById(userAddressRequest.getDistrictId()).get());
        }
    }

    @BeanMapping(qualifiedByName = "updateAddressExist", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "address", target = "address")
    public abstract void updateAddress(UserAddressRequest dto, @MappingTarget UserAddress entity);
}
