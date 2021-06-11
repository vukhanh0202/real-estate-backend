package com.uit.realestate.mapper.location;

import com.uit.realestate.domain.location.Country;
import com.uit.realestate.dto.location.CountryDto;
import com.uit.realestate.mapper.MapperBase;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public abstract class CountryMapper implements MapperBase {

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "code", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "phoneCode", target = "phoneCode")
    public abstract CountryDto toCountryDto(Country country);

    @BeanMapping(ignoreByDefault = true)
    public abstract List<CountryDto> toCountryDtoList(List<Country> countryList);
}
