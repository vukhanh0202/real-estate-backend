package com.uit.realestate.mapper.user;

import com.uit.realestate.domain.user.User;
import com.uit.realestate.domain.user.UserTarget;
import com.uit.realestate.dto.user.UserDetailDto;
import com.uit.realestate.dto.user.UserDto;
import com.uit.realestate.dto.user.UserTargetDto;
import com.uit.realestate.mapper.MapperBase;
import com.uit.realestate.mapper.address.UserAddressMapper;
import com.uit.realestate.payload.user.AddUserTargetRequest;
import com.uit.realestate.payload.user.UpdateAvatarUserRequest;
import com.uit.realestate.payload.user.UpdateUserRequest;
import com.uit.realestate.payload.user.UpdateUserTargetRequest;
import com.uit.realestate.repository.category.CategoryRepository;
import com.uit.realestate.repository.location.DistrictRepository;
import com.uit.realestate.repository.location.ProvinceRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public abstract class UserMapper implements MapperBase {

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Named("toUserDto")
    @BeforeMapping
    protected void toApartmentPreviewDto(User user, @MappingTarget UserDto userDto) {
        userDto.setAddressDto(userAddressMapper.toUserAddressDto(user.getUserAddress()));
    }

    @Named("toUserTargetDto")
    @BeforeMapping
    protected void toUserTargetDto(UserTarget userTarget, @MappingTarget UserTargetDto dto) {
        if (userTarget.getDistrict() != null){
            dto.setDistrictName(districtRepository.findById(userTarget.getDistrict()).orElse(null).getName());
        }
        if (userTarget.getProvince() != null){
            dto.setProvinceName(provinceRepository.findById(userTarget.getProvince()).orElse(null).getName());
        }
        if (userTarget.getCategory() != null){
            dto.setCategoryName(categoryRepository.findById(userTarget.getCategory()).orElse(null).getName());
        }
    }

    @BeanMapping(qualifiedByName = "toUserDto", ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "avatar", target = "avatar", qualifiedByName = "getFile")
    public abstract UserDto toUserDto(User user);

    @BeanMapping(ignoreByDefault = true)
    public abstract List<UserDto> toUserDtoList(List<User> users);

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "createdAt", target = "createdAt")
    public abstract UserDetailDto toUserDetailDto(User user);

    @Named("updateUserAddress")
    @BeforeMapping
    protected void updateUserAddress(UpdateUserRequest dto, @MappingTarget User entity) {
        userAddressMapper.updateAddress(dto.getAddress(),entity.getUserAddress());
    }

    @BeanMapping(qualifiedByName = "updateUserAddress", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "description", target = "description")
    public abstract void updateUser(UpdateUserRequest dto, @MappingTarget User entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "photo", target = "avatar", qualifiedByName = "setFile")
    public abstract void updateAvatarUser(UpdateAvatarUserRequest dto, @MappingTarget User entity);

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "districtId", target = "district")
    @Mapping(source = "provinceId", target = "province")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "floorQuantity", target = "floorQuantity")
    @Mapping(source = "bedroomQuantity", target = "bedroomQuantity")
    @Mapping(source = "bathroomQuantity", target = "bathroomQuantity")
    @Mapping(source = "area", target = "area")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "typeApartment", target = "typeApartment")
    public abstract UserTarget toUserTarget(AddUserTargetRequest userTargetDto);

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "districtId", target = "district")
    @Mapping(source = "provinceId", target = "province")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "floorQuantity", target = "floorQuantity")
    @Mapping(source = "bedroomQuantity", target = "bedroomQuantity")
    @Mapping(source = "bathroomQuantity", target = "bathroomQuantity")
    @Mapping(source = "area", target = "area")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "typeApartment", target = "typeApartment")
    public abstract void updateUserTarget(UpdateUserTargetRequest dto, @MappingTarget UserTarget entity);

    @BeanMapping(qualifiedByName = "toUserTargetDto")
    public abstract UserTargetDto toUserTargetDto(UserTarget userTarget);

    public abstract List<UserTargetDto> toUserTargetDtoList(List<UserTarget> userTargets);
}
