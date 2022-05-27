package com.uit.realestate.mapper.apartment;

import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.domain.apartment.ApartmentRating;
import com.uit.realestate.domain.tracking.TrackingCategory;
import com.uit.realestate.domain.tracking.TrackingDistrict;
import com.uit.realestate.domain.tracking.TrackingProvince;
import com.uit.realestate.domain.tracking.TrackingTypeApartment;
import com.uit.realestate.domain.user.User;
import com.uit.realestate.dto.apartment.ApartmentBasicDto;
import com.uit.realestate.dto.apartment.ApartmentCompareDto;
import com.uit.realestate.dto.apartment.ApartmentDto;
import com.uit.realestate.dto.apartment.ApartmentSearchDto;
import com.uit.realestate.mapper.MapperBase;
import com.uit.realestate.payload.apartment.AddApartmentRequest;
import com.uit.realestate.payload.apartment.UpdateApartmentRequest;
import com.uit.realestate.repository.action.FavouriteRepository;
import com.uit.realestate.repository.category.CategoryRepository;
import com.uit.realestate.repository.location.DistrictRepository;
import com.uit.realestate.repository.location.ProvinceRepository;
import com.uit.realestate.repository.tracking.TrackingCategoryRepository;
import com.uit.realestate.repository.tracking.TrackingDistrictRepository;
import com.uit.realestate.repository.tracking.TrackingProvinceRepository;
import com.uit.realestate.repository.tracking.TrackingTypeApartmentRepository;
import com.uit.realestate.repository.user.UserRepository;
import com.uit.realestate.utils.StringUtils;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public abstract class ApartmentMapper implements MapperBase {

    @Autowired
    private ApartmentDetailMapper apartmentDetailMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ApartmentAddressMapper apartmentAddressMapper;

    @Autowired
    private FavouriteRepository favouriteRepository;

    @Autowired
    private SuitabilityMapper suitabilityMapper;

    @Autowired
    private TrackingCategoryRepository trackingCategoryRepository;

    @Autowired
    private TrackingTypeApartmentRepository trackingTypeApartmentRepository;

    @Autowired
    private TrackingDistrictRepository trackingDistrictRepository;

    @Autowired
    private TrackingProvinceRepository trackingProvinceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private DistrictRepository districtRepository;

    private Double calculateSuitableWithIpAndUserId(Apartment apartment, String ip, Long userId){
        if (ip != null && userId != null){
            List<TrackingCategory> categories = trackingCategoryRepository.findAllByUserIdOrIp(userId, ip);
            List<TrackingTypeApartment> typeApartments = trackingTypeApartmentRepository.findAllByUserIdOrIp(userId, ip);
            List<TrackingDistrict> districts = trackingDistrictRepository.findAllByUserIdOrIp(userId, ip);
            List<TrackingProvince> provinces = trackingProvinceRepository.findAllByUserIdOrIp(userId, ip);
            Double total = 0D;
            total += (double) categories.stream().filter(item -> item.getCategory().getId().equals(apartment.getCategory().getId())).mapToLong(TrackingCategory::getRating).sum()
                    /  categories.stream().mapToLong(TrackingCategory::getRating).sum() * 100;

            total += (double) typeApartments.stream().filter(item -> item.getTypeApartment().equals(apartment.getTypeApartment())).mapToLong(TrackingTypeApartment::getRating).sum()
                    /  typeApartments.stream().mapToLong(TrackingTypeApartment::getRating).sum() * 100;

            total += (double) districts.stream().filter(item -> item.getDistrict().getId().equals(apartment.getApartmentAddress().getDistrict().getId())).mapToLong(TrackingDistrict::getRating).sum()
                    /  districts.stream().mapToLong(TrackingDistrict::getRating).sum() * 100;

            total += (double) provinces.stream().filter(item -> item.getProvince().getId().equals(apartment.getApartmentAddress().getProvince().getId())).mapToLong(TrackingProvince::getRating).sum()
                    /  provinces.stream().mapToLong(TrackingProvince::getRating).sum() * 100;
            return Math.ceil(total / 4);
        }
        return null;
    }

    private Double calculateSuitableWithIpAndUserId(ApartmentRating apartment, String ip, Long userId){
        if (ip != null && userId != null){
            List<TrackingCategory> categories = trackingCategoryRepository.findAllByUserIdOrIp(userId, ip);
            List<TrackingTypeApartment> typeApartments = trackingTypeApartmentRepository.findAllByUserIdOrIp(userId, ip);
            List<TrackingDistrict> districts = trackingDistrictRepository.findAllByUserIdOrIp(userId, ip);
            List<TrackingProvince> provinces = trackingProvinceRepository.findAllByUserIdOrIp(userId, ip);
            Double total = 0D;
            total += (double) categories.stream().filter(item -> item.getCategory().getId().equals(apartment.getCategoryId())).mapToLong(TrackingCategory::getRating).sum()
                    /  categories.stream().mapToLong(TrackingCategory::getRating).sum() * 100;

            total += (double) typeApartments.stream().filter(item -> item.getTypeApartment().equals(apartment.getTypeApartment())).mapToLong(TrackingTypeApartment::getRating).sum()
                    /  typeApartments.stream().mapToLong(TrackingTypeApartment::getRating).sum() * 100;

            total += (double) districts.stream().filter(item -> item.getDistrict().getId().equals(apartment.getDistrictId())).mapToLong(TrackingDistrict::getRating).sum()
                    /  districts.stream().mapToLong(TrackingDistrict::getRating).sum() * 100;

            total += (double) provinces.stream().filter(item -> item.getProvince().getId().equals(apartment.getProvinceId())).mapToLong(TrackingProvince::getRating).sum()
                    /  provinces.stream().mapToLong(TrackingProvince::getRating).sum() * 100;
            return Math.ceil(total / 4);
        }
        return null;
    }

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

    //*************************************************
    //********** Mapper Apartment To ApartmentBasicDto (Search) **********
    //*************************************************
    @Named("toApartmentPreviewDto")
    @BeforeMapping
    protected void toApartmentPreviewDto(Apartment apartment, @MappingTarget ApartmentDto dto, @Context Long userId, @Context String ip) {
        if (apartment.getApartmentAddress() != null) {
            dto.setAddress(apartment.getApartmentAddress().getDistrict().getName()
                    + ", " + apartment.getApartmentAddress().getProvince().getName());
            dto.setAddressDetail(apartmentAddressMapper.toApartmentAddressDto(apartment.getApartmentAddress()));
        }
        if (userId != null && favouriteRepository.findByApartmentIdAndUserId(apartment.getId(), userId).isPresent()) {
            dto.setFavourite(true);
        }
        dto.setTypeApartment(apartment.getTypeApartment().getValue());
        dto.setTotalPrice(convertPriceToString(apartment));
        dto.setPercentSuitable(calculateSuitableWithIpAndUserId(apartment, ip, userId));
    }

    @BeanMapping(qualifiedByName = "toApartmentPreviewDto", ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Named("toApartmentPreviewDtoList")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "area", target = "area")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "apartmentDetail.bedroomQuantity", target = "bedroomQuantity")
    @Mapping(source = "apartmentDetail.bathroomQuantity", target = "bathroomQuantity")
    @Mapping(source = "apartmentDetail.floorQuantity", target = "floorQuantity")
    @Mapping(source = "photos", target = "photos", qualifiedByName = "getFiles")
    @Mapping(source = "highlight", target = "isHighlight")
    @Mapping(source = "author", target = "author", qualifiedByName = "getUserInfo")
    public abstract ApartmentDto toApartmentPreviewDto(Apartment apartment, @Context Long userId, @Context String ip);

    @BeanMapping(ignoreByDefault = true)
    @IterableMapping(qualifiedByName = "toApartmentPreviewDtoList")
    public abstract List<ApartmentDto> toApartmentPreviewDtoList(List<Apartment> apartmentList, @Context Long userId, @Context String ip);

    @Named("toApartmentRatingPreviewDto")
    @BeforeMapping
    protected void toApartmentRatingPreviewDto(ApartmentRating apartment, @MappingTarget ApartmentDto dto, @Context Long userId, @Context String ip) {
        dto.setId(apartment.getId());
        dto.setTitle(apartment.getTitle());
        dto.setArea(apartment.getArea());
        dto.setStatus(apartment.getStatus());
        dto.setCategoryName(apartment.getCategoryName());
        dto.setPhotos(getFiles(apartment.getPhotos()));
        dto.setIsHighlight(apartment.getHighlight());
        dto.setAuthor(getUserInfo(userRepository.findById(apartment.getAuthorId()).orElse(new User())));
        dto.setBedroomQuantity(apartment.getBedroomQuantity());
        dto.setBathroomQuantity(apartment.getBathroomQuantity());
        dto.setFloorQuantity(apartment.getFloorQuantity());
        dto.setAddress(districtRepository.findById(apartment.getDistrictId()).get().getName() +", "+ provinceRepository.findById(apartment.getProvinceId()).get().getName());
        dto.setTypeApartment(apartment.getTypeApartment());
        if (apartment.getTypeApartment().equals(ETypeApartment.BUY.name())) {
            dto.setTotalPrice( StringUtils.castPriceFromNumber(apartment.getTotal_Price()));
        } else {
            if (apartment.getUnit_Rent() == null) {
                dto.setTotalPrice("0");
            }
            dto.setTotalPrice(apartment.getUnit_Rent().trim());
        }

        if (userId != null && favouriteRepository.findByApartmentIdAndUserId(apartment.getId(), userId).isPresent()) {
            dto.setFavourite(true);
        }
        dto.setPercentSuitable(calculateSuitableWithIpAndUserId(apartment, ip, userId));
    }

    @BeanMapping(qualifiedByName = "toApartmentRatingPreviewDto", ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract ApartmentDto toApartmentRatingPreviewDto(ApartmentRating apartment, @Context Long userId, @Context String ip);

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract List<ApartmentDto> toApartmentRatingPreviewDtoList(List<ApartmentRating> apartmentList, @Context Long userId, @Context String ip);

    //*************************************************
    //********** Mapper Apartment To ApartmentSearchDto (Search All) **********
    //*************************************************
    @Named("toApartmentSearchDto")
    @BeforeMapping
    protected void toApartmentSearchDto(Apartment apartment, @MappingTarget ApartmentSearchDto dto) {
        if (apartment.getApartmentAddress() != null) {
            dto.setAddress(apartment.getApartmentAddress().getDistrict().getName()
                    + ", " + apartment.getApartmentAddress().getProvince().getName());
            dto.setAddressDetail(apartmentAddressMapper.toApartmentAddressDto(apartment.getApartmentAddress()));
        }
        dto.setTotalPrice(convertPriceToString(apartment));
    }

    @BeanMapping(qualifiedByName = "toApartmentSearchDto", ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Named("toApartmentSearchDtoList")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "area", target = "area")
    public abstract ApartmentSearchDto toApartmentSearchDto(Apartment apartment);

    @BeanMapping(ignoreByDefault = true)
    @IterableMapping(qualifiedByName = "toApartmentSearchDtoList")
    public abstract List<ApartmentSearchDto> toApartmentSearchDtoList(List<Apartment> apartmentList);

    //*************************************************
    //********** Mapper Apartment To ApartmentFullDto (Detail) **********
    //*************************************************
    @Named("toApartmentFullDto")
    @BeforeMapping
    protected void toApartmentFullDto(Apartment apartment, @MappingTarget ApartmentDto dto, @Context Long userId) {
        dto.setAddress(apartment.getApartmentAddress().getDistrict().getName()
                + ", " + apartment.getApartmentAddress().getProvince().getName());
        dto.setApartmentDetail(apartmentDetailMapper.toApartmentDetailDto(apartment.getApartmentDetail()));
        dto.setAddressDetail(apartmentAddressMapper.toApartmentAddressDto(apartment.getApartmentAddress()));
        if (userId != null && favouriteRepository.findByApartmentIdAndUserId(apartment.getId(), userId).isPresent()) {
            dto.setFavourite(true);
        }
        dto.setTypeApartment(apartment.getTypeApartment().getValue());
        dto.setTotalPrice(convertPriceToString(apartment));
    }

    @BeanMapping(qualifiedByName = "toApartmentFullDto", ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "area", target = "area")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "photos", target = "photos", qualifiedByName = "getFiles")
    @Mapping(source = "author", target = "author", qualifiedByName = "getUserInfo")
    public abstract ApartmentDto toApartmentFullDto(Apartment apartment, @Context Long userId);


    //*************************************************
    //********** Mapper Apartment To Apartment Basic **********
    //*************************************************
    @Named("toApartmentBasicDto")
    @BeforeMapping
    protected void toApartmentBasicDto(Apartment apartment, @MappingTarget ApartmentBasicDto dto, @Context Long userId, @Context String ip) {
        dto.setAddress(apartment.getApartmentAddress().getDistrict().getName()
                + ", " + apartment.getApartmentAddress().getProvince().getName());
        if (userId != null && favouriteRepository.findByApartmentIdAndUserId(apartment.getId(), userId).isPresent()) {
            dto.setFavourite(true);
        }
        dto.setTypeApartment(apartment.getTypeApartment().getValue());
        dto.setTotalPrice(convertPriceToString(apartment));
        dto.setPercentSuitable(calculateSuitableWithIpAndUserId(apartment, ip, userId));
    }

    @BeanMapping(qualifiedByName = "toApartmentBasicDto", ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "apartmentDetail.bedroomQuantity", target = "bedroomQuantity")
    @Mapping(source = "apartmentDetail.bathroomQuantity", target = "bathroomQuantity")
    @Mapping(source = "area", target = "area")
    @Mapping(source = "createdBy", target = "createdBy", qualifiedByName = "getAudit")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "photos", target = "photos", qualifiedByName = "getFiles")
    @Mapping(source = "author", target = "author", qualifiedByName = "getUserInfo")
    public abstract ApartmentBasicDto toApartmentBasicDto(Apartment apartment, @Context Long userId, @Context String ip);

    @BeanMapping(ignoreByDefault = true)
    public abstract List<ApartmentBasicDto> toApartmentBasicDtoList(List<Apartment> apartmentList, @Context Long userId, @Context String ip);

    //*************************************************
    //********** Mapper AddApartmentRequest To Apartment (Entity) **********
    //*************************************************
    @Named("toApartment")
    @BeforeMapping
    protected void toApartment(AddApartmentRequest addApartmentRequest, @MappingTarget Apartment apartment) {
        apartment.setCategory(categoryRepository.findById(addApartmentRequest.getCategoryId()).get());
        apartment.setApartmentAddress(apartmentAddressMapper.toApartmentAddress(addApartmentRequest.getApartmentAddress()));
        apartment.setApartmentDetail(apartmentDetailMapper.toApartmentDetail(addApartmentRequest.getApartmentDetail()));
        if (addApartmentRequest.getTypeApartment().equals(ETypeApartment.BUY)) {
            apartment.setTotalPrice(addApartmentRequest.getTotalPrice());
            apartment.setPrice((double) Math.round(addApartmentRequest.getTotalPrice() / addApartmentRequest.getArea()));
        } else {
            apartment.setPriceRent(addApartmentRequest.getPriceRent());
            apartment.setUnitRent(addApartmentRequest.getUnitRent());
        }
    }

    @BeanMapping(qualifiedByName = "toApartment", ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "title", target = "title")
    @Mapping(source = "area", target = "area")
    @Mapping(source = "typeApartment", target = "typeApartment")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "authorId", target = "author.id")
    @Mapping(source = "photos", target = "photos", qualifiedByName = "setFiles")
    public abstract Apartment toApartment(AddApartmentRequest addApartmentRequest);

    @Named("updateApartmentMapping")
    @BeforeMapping
    protected void updateApartmentMapping(UpdateApartmentRequest updateApartmentRequest, @MappingTarget Apartment apartment) {
        if (updateApartmentRequest.getCategoryId() != null) {
            apartment.setCategory(categoryRepository.findById(updateApartmentRequest.getCategoryId()).get());
        }
        if (updateApartmentRequest.getApartmentAddress() != null) {
            apartmentAddressMapper.updateApartmentAddress(updateApartmentRequest.getApartmentAddress(), apartment.getApartmentAddress());
        }
        if (updateApartmentRequest.getApartmentDetail() != null) {
            apartmentDetailMapper.updateApartmentDetail(updateApartmentRequest.getApartmentDetail(), apartment.getApartmentDetail());
        }
        if (updateApartmentRequest.getTotalPrice() != null || updateApartmentRequest.getArea() != null)
            apartment.setPrice((double) Math.round(updateApartmentRequest.getTotalPrice() / updateApartmentRequest.getArea()));
        if (updateApartmentRequest.getTypeApartment().equals(ETypeApartment.BUY)) {
            apartment.setTotalPrice(updateApartmentRequest.getTotalPrice());
            apartment.setPrice((double) Math.round(updateApartmentRequest.getTotalPrice() / updateApartmentRequest.getArea()));
        } else {
            apartment.setPriceRent(updateApartmentRequest.getPriceRent());
            apartment.setUnitRent(updateApartmentRequest.getUnitRent());
        }
    }

    @BeanMapping(qualifiedByName = "updateApartmentMapping", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "title", target = "title")
    @Mapping(source = "area", target = "area")
    @Mapping(source = "totalPrice", target = "totalPrice")
    @Mapping(source = "typeApartment", target = "typeApartment")
    @Mapping(source = "photos", target = "photos", qualifiedByName = "setFiles")
    public abstract void updateApartment(UpdateApartmentRequest dto, @MappingTarget Apartment apartment);


    //*************************************************
    //********** Mapper AddApartmentRequest To Apartment (Entity) **********
    //*************************************************
    @Named("toApartmentCompareDto")
    @BeforeMapping
    protected void toApartmentCompareDto(Apartment apartment, @MappingTarget ApartmentCompareDto apartmentCompareDto, @Context Long userId) {
        apartmentCompareDto.setAddress(apartment.getApartmentAddress().getAddress() + ", "
                + apartment.getApartmentAddress().getDistrict().getName() + ", "
                + apartment.getApartmentAddress().getProvince().getName() + ", "
                + apartment.getApartmentAddress().getCountry().getName());
        apartmentCompareDto.setTotalPrice(convertPriceToString(apartment));
    }

    @BeanMapping(qualifiedByName = "toApartmentCompareDto", ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "area", target = "area")
    @Mapping(source = "typeApartment", target = "typeApartment")
    @Mapping(source = "apartmentDetail.bedroomQuantity", target = "bedroomQuantity")
    @Mapping(source = "apartmentDetail.bathroomQuantity", target = "bathroomQuantity")
    @Mapping(source = "apartmentDetail.floorQuantity", target = "floorQuantity")
    @Mapping(source = "apartmentDetail.houseDirection", target = "houseDirection")
    @Mapping(source = "apartmentDetail.toiletQuantity", target = "toiletQuantity")
    @Mapping(source = "apartmentDetail.moreInfo", target = "moreInfo")
    @Mapping(source = "photos", target = "photos", qualifiedByName = "getFiles")
    public abstract ApartmentCompareDto toApartmentCompareDto(Apartment apartment, @Context Long userId);

    @BeanMapping(ignoreByDefault = true)
    public abstract List<ApartmentCompareDto> toApartmentCompareDtoList(List<Apartment> apartmentList, @Context Long userId);

}
