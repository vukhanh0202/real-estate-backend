package com.uit.realestate.mapper;

import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.domain.apartment.Category;
import com.uit.realestate.domain.location.Country;
import com.uit.realestate.dto.apartment.ThumbnailChatDto;
import com.uit.realestate.dto.category.CategoryDto;
import com.uit.realestate.dto.location.CountryDto;
import com.uit.realestate.utils.StringUtils;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public abstract class TrackingChatbotMapper implements MapperBase {

    @Value("${host}")
    private String host;

    @Value("${host-fe}")
    private String hostFE;

    private final String FOLDER_IMAGE = "/api/public/image/apartment/";

    @Named("toThumbnailChatDto")
    @BeforeMapping
    protected void toThumbnailChatDto(Apartment apartment, @MappingTarget ThumbnailChatDto dto) {
        dto.setTitle(StringUtils.concat(apartment.getTitle(), 100));
        dto.setSubtitle("Diện tích: " + apartment.getArea());
        dto.setImage(host + FOLDER_IMAGE + getFiles(apartment.getPhotos()).get(0).getName());
        dto.setLink(hostFE);
    }

    @BeanMapping(qualifiedByName = "toThumbnailChatDto", ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "id", target = "id")
    public abstract ThumbnailChatDto toThumbnailChatDto(Apartment apartment);

    @BeanMapping(ignoreByDefault = true)
    public abstract List<ThumbnailChatDto> toThumbnailChatDtoList(List<Apartment> apartments);
}
