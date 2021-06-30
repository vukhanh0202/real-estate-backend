package com.uit.realestate.mapper.category;

import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.domain.apartment.Category;
import com.uit.realestate.dto.category.CategoryDto;
import com.uit.realestate.mapper.MapperBase;
import com.uit.realestate.payload.category.CategoryRequest;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public abstract class CategoryMapper implements MapperBase {

    @Named("toCategoryDto")
    @BeforeMapping
    protected void toCategoryDto(Category category, @MappingTarget CategoryDto categoryDto) {
        categoryDto.setTotalItem(category.getApartments().stream().filter(apartment -> apartment.getStatus().equals(EApartmentStatus.OPEN)).count());
    }

    @BeanMapping(qualifiedByName = "toCategoryDto", ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    public abstract CategoryDto toCategoryDto(Category category);

    @BeanMapping(ignoreByDefault = true)
    public abstract List<CategoryDto> toCategoryDtoList(List<Category> categoryList);

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "name", target = "name")
    public abstract Category toCategory(CategoryRequest categoryRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "name", target = "name")
    public abstract void updateCategory(CategoryRequest dto, @MappingTarget Category category);
}
