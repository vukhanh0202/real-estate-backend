package com.uit.realestate.mapper;

import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.dto.apartment.ThumbnailChatDto;
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
    private final String LINK_DETAIL_APARTMENT = "/chi-tiet/";

    @Named("toThumbnailChatDto")
    @BeforeMapping
    protected void toThumbnailChatDto(Apartment apartment, @MappingTarget ThumbnailChatDto dto) {
        dto.setTitle(StringUtils.concat(apartment.getTitle(), 100).toUpperCase());
        StringBuilder str = new StringBuilder();
        str.append("Diện tích: ").append(StringUtils.castPriceFromNumber(apartment.getArea())).append("m2");
        str.append("  -  ");
        str.append("Giá : ").append(convertPriceToString(apartment));
        str.append("\n");
        str.append("Thể loại: ").append(apartment.getTypeApartment().getDisplayName());
        dto.setSubtitle(str.toString());
        if (!apartment.getPhotos().isEmpty()) {
            dto.setImage(host + FOLDER_IMAGE + getFiles(apartment.getPhotos()).get(0).getName());
        }
        dto.setLink(hostFE + LINK_DETAIL_APARTMENT + apartment.getId());
    }

    @BeanMapping(qualifiedByName = "toThumbnailChatDto", ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "id", target = "id")
    public abstract ThumbnailChatDto toThumbnailChatDto(Apartment apartment);

    @BeanMapping(ignoreByDefault = true)
    public abstract List<ThumbnailChatDto> toThumbnailChatDtoList(List<Apartment> apartments);

    private String convertPriceToString(Apartment apartment) {
        if (apartment.getTypeApartment().equals(ETypeApartment.BUY)) {
            return StringUtils.castPriceFromNumber(apartment.getTotalPrice());
        } else {
            if (apartment.getUnitRent() == null) {
                return "0";
            }
            return apartment.getUnitRent().trim();
        }
    }
}
