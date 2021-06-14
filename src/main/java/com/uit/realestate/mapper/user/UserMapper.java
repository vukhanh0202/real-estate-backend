package com.uit.realestate.mapper.user;

import com.uit.realestate.domain.user.User;
import com.uit.realestate.dto.user.UserDto;
import com.uit.realestate.mapper.MapperBase;
import com.uit.realestate.mapper.address.UserAddressMapper;
import com.uit.realestate.payload.user.UpdateUserRequest;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public abstract class UserMapper implements MapperBase {

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Named("toUserDto")
    @BeforeMapping
    protected void toApartmentPreviewDto(User user, @MappingTarget UserDto userDto) {
        userDto.setAddressDto(userAddressMapper.toUserAddressDto(user.getUserAddress()));
    }

    @BeanMapping(qualifiedByName = "toUserDto", ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "description", target = "description")
    public abstract UserDto toUserDto(User user);

    @BeanMapping(ignoreByDefault = true)
    public abstract List<UserDto> toUserDtoList(List<User> users);


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
}
