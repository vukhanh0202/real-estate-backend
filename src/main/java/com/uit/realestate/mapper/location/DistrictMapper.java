package com.uit.realestate.mapper.location;

import com.uit.realestate.domain.location.District;
import com.uit.realestate.dto.location.DistrictDto;
import com.uit.realestate.mapper.MapperBase;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class DistrictMapper implements MapperBase {

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "shortName", target = "shortName")
    public abstract DistrictDto toProvinceDto(District district);

    @BeanMapping(ignoreByDefault = true)
    public abstract List<DistrictDto> toProvinceDtoList(List<District> districtList);
}
