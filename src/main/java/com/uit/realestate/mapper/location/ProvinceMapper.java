package com.uit.realestate.mapper.location;

import com.uit.realestate.domain.location.Country;
import com.uit.realestate.domain.location.Province;
import com.uit.realestate.dto.location.CountryDto;
import com.uit.realestate.dto.location.ProvinceDto;
import com.uit.realestate.mapper.MapperBase;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class ProvinceMapper implements MapperBase {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "shortName", target = "shortName")
    @Mapping(source = "isCity", target = "isCity")
    public abstract ProvinceDto toProvinceDto(Province province);

    @BeanMapping(ignoreByDefault = true)
    public abstract List<ProvinceDto> toProvinceDtoList(List<Province> provinceList);
}
