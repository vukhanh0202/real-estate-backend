package com.uit.realestate.mapper.category;

import com.uit.realestate.domain.apartment.Category;
import com.uit.realestate.domain.location.Country;
import com.uit.realestate.dto.category.CategoryDto;
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
public abstract class CategoryMapper implements MapperBase {

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    public abstract CategoryDto toCategoryDto(Category category);

    @BeanMapping(ignoreByDefault = true)
    public abstract List<CategoryDto> toCategoryDtoList(List<Category> categoryList);
}
