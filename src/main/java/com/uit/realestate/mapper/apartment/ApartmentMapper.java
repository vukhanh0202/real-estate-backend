package com.uit.realestate.mapper.apartment;

import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.domain.apartment.ApartmentRating;
import com.uit.realestate.domain.tracking.*;
import com.uit.realestate.domain.user.User;
import com.uit.realestate.dto.apartment.ApartmentBasicDto;
import com.uit.realestate.dto.apartment.ApartmentCompareDto;
import com.uit.realestate.dto.apartment.ApartmentDto;
import com.uit.realestate.dto.apartment.ApartmentSearchDto;
import com.uit.realestate.mapper.MapperBase;
import com.uit.realestate.payload.apartment.AddApartmentRequest;
import com.uit.realestate.payload.apartment.UpdateApartmentRequest;
import com.uit.realestate.repository.action.FavouriteRepository;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.repository.category.CategoryRepository;
import com.uit.realestate.repository.location.DistrictRepository;
import com.uit.realestate.repository.location.ProvinceRepository;
import com.uit.realestate.repository.tracking.*;
import com.uit.realestate.repository.user.UserRepository;
import com.uit.realestate.utils.StringUtils;
import lombok.AllArgsConstructor;
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
    @Autowired
    private TrackingAreaRepository trackingAreaRepository;
    @Autowired
    private TrackingBedroomRepository trackingBedroomRepository;
    @Autowired
    private TrackingBathRoomRepository trackingBathRoomRepository;
    @Autowired
    private TrackingDirectionRepository trackingDirectionRepository;
    @Autowired
    private TrackingFloorRepository trackingFloorRepository;
    @Autowired
    private TrackingPriceRepository trackingPriceRepository;
    @Autowired
    private TrackingToiletRepository trackingToiletRepository;
    @Autowired
    private ApartmentRepository apartmentRepository;

    private int countValue(double result) {
        if (result > 0) {
            return 1;
        }
        return 0;
    }

    private Double getRating(Long id, String ip, Long userId) {
        if (userId == null) {
            userId = -1L;
        }
        if (ip == null) {
            return null;
        }
        ApartmentRating apartment = apartmentRepository.findApartmentDetailRatingByUserIdAndIp(id, userId, ip).stream().findFirst().orElse(null);
        if (apartment == null){
            return null;
        }
        return apartment.getRating();
    }

    private Double calculateSuitableWithIpAndUserId(Apartment apartment, String ip, Long userId) {
        if (ip != null && userId != null) {
            List<TrackingCategory> categories = trackingCategoryRepository.findAllByUserIdOrIp(userId, ip);
            List<TrackingTypeApartment> typeApartments = trackingTypeApartmentRepository.findAllByUserIdOrIp(userId, ip);
            List<TrackingDistrict> districts = trackingDistrictRepository.findAllByUserIdOrIp(userId, ip);
            List<TrackingProvince> provinces = trackingProvinceRepository.findAllByUserIdOrIp(userId, ip);
            List<TrackingArea> areas = trackingAreaRepository.findAllByUserIdOrIp(userId, ip);
            List<TrackingBedroom> bedrooms = trackingBedroomRepository.findAllByUserIdOrIp(userId, ip);
            List<TrackingBathroom> bathrooms = trackingBathRoomRepository.findAllByUserIdOrIp(userId, ip);
            List<TrackingDirection> directions = trackingDirectionRepository.findAllByUserIdOrIp(userId, ip);
            List<TrackingFloor> floors = trackingFloorRepository.findAllByUserIdOrIp(userId, ip);
            List<TrackingPrice> prices = trackingPriceRepository.findAllByUserIdOrIp(userId, ip);
            List<TrackingToilet> toilets = trackingToiletRepository.findAllByUserIdOrIp(userId, ip);
            double total = 0D;
            int count = 0;
            double value;

            value = (double) categories.stream().filter(item -> item.getCategory().getId().equals(apartment.getCategory().getId())).mapToLong(TrackingCategory::getRating).sum()
                    / categories.stream().mapToLong(TrackingCategory::getRating).sum() * 100;
            count += countValue(value);
            total += value;
            value = (double) typeApartments.stream().filter(item -> item.getTypeApartment().equals(apartment.getTypeApartment())).mapToLong(TrackingTypeApartment::getRating).sum()
                    / typeApartments.stream().mapToLong(TrackingTypeApartment::getRating).sum() * 100;
            count += countValue(value);
            total += value;
            value = (double) districts.stream().filter(item -> item.getDistrict().getId().equals(apartment.getApartmentAddress().getDistrict().getId())).mapToLong(TrackingDistrict::getRating).sum()
                    / districts.stream().mapToLong(TrackingDistrict::getRating).sum() * 100;
            count += countValue(value);
            total += value;
            value = (double) provinces.stream().filter(item -> item.getProvince().getId().equals(apartment.getApartmentAddress().getProvince().getId())).mapToLong(TrackingProvince::getRating).sum()
                    / provinces.stream().mapToLong(TrackingProvince::getRating).sum() * 100;
            count += countValue(value);
            total += value;
            value = (double) areas.stream().filter(item -> item.getArea().equals(apartment.getArea())).mapToLong(TrackingArea::getRating).sum()
                    / areas.stream().mapToLong(TrackingArea::getRating).sum() * 100;
            count += countValue(value);
            total += value;
            value = (double) bedrooms.stream().filter(item -> item.getBedroom().equals(apartment.getApartmentDetail().getBedroomQuantity())).mapToLong(TrackingBedroom::getRating).sum()
                    / bedrooms.stream().mapToLong(TrackingBedroom::getRating).sum() * 100;
            count += countValue(value);
            total += value;
            value = (double) bathrooms.stream().filter(item -> item.getBathroom().equals(apartment.getApartmentDetail().getBathroomQuantity())).mapToLong(TrackingBathroom::getRating).sum()
                    / bathrooms.stream().mapToLong(TrackingBathroom::getRating).sum() * 100;
            count += countValue(value);
            total += value;
            value = (double) directions.stream().filter(item -> item.getDirection().equals(apartment.getApartmentDetail().getHouseDirection())).mapToLong(TrackingDirection::getRating).sum()
                    / directions.stream().mapToLong(TrackingDirection::getRating).sum() * 100;
            count += countValue(value);
            total += value;
            value = (double) floors.stream().filter(item -> item.getFloor().equals(apartment.getApartmentDetail().getFloorQuantity())).mapToLong(TrackingFloor::getRating).sum()
                    / floors.stream().mapToLong(TrackingFloor::getRating).sum() * 100;
            count += countValue(value);
            total += value;
            value = (double) toilets.stream().filter(item -> item.getToilet().equals(apartment.getApartmentDetail().getToiletQuantity())).mapToLong(TrackingToilet::getRating).sum()
                    / toilets.stream().mapToLong(TrackingToilet::getRating).sum() * 100;
            count += countValue(value);
            total += value;
            value = (double) prices.stream().filter(item -> {
                if (apartment.getTypeApartment().equals(ETypeApartment.BUY)) {
                    return item.getPrice().equals(apartment.getTotalPrice());
                }
                return item.getPrice().equals(apartment.getPriceRent());
            }).mapToLong(TrackingPrice::getRating).sum()
                    / prices.stream().mapToLong(TrackingPrice::getRating).sum() * 100;
            count += countValue(value);
            total += value;

            if (count > 0) {
                return Math.ceil(total / count);
            }
            return 0D;
        }
        return null;
    }

    private Double calculateSuitableWithIpAndUserId(Long id, String ip, Long userId) {
        return calculateSuitableWithIpAndUserId(getRating(id, ip, userId), ip, userId);
    }

    private Double calculateSuitableWithIpAndUserId(Double rating, String ip, Long userId) {
        if (ip != null && userId != null) {
            int count = 0;
            if (!trackingCategoryRepository.findAllByUserIdOrIp(userId, ip).isEmpty()) {
                count++;
            }
            ;
            if (!trackingTypeApartmentRepository.findAllByUserIdOrIp(userId, ip).isEmpty()) {
                count++;
            }
            ;
            if (!trackingDistrictRepository.findAllByUserIdOrIp(userId, ip).isEmpty()) {
                count++;
            }
            ;
            if (!trackingProvinceRepository.findAllByUserIdOrIp(userId, ip).isEmpty()) {
                count++;
            }
            ;
            if (!trackingAreaRepository.findAllByUserIdOrIp(userId, ip).isEmpty()) {
                count++;
            }
            ;
            if (!trackingBedroomRepository.findAllByUserIdOrIp(userId, ip).isEmpty()) {
                count++;
            }
            ;
            if (!trackingBathRoomRepository.findAllByUserIdOrIp(userId, ip).isEmpty()) {
                count++;
            }
            ;
            if (!trackingDirectionRepository.findAllByUserIdOrIp(userId, ip).isEmpty()) {
                count++;
            }
            ;
            if (!trackingFloorRepository.findAllByUserIdOrIp(userId, ip).isEmpty()) {
                count++;
            }
            ;
            if (!trackingPriceRepository.findAllByUserIdOrIp(userId, ip).isEmpty()) {
                count++;
            }
            ;
            if (!trackingToiletRepository.findAllByUserIdOrIp(userId, ip).isEmpty()) {
                count++;
            }
            ;

            if (count > 0) {
                return Math.ceil(rating / count * 100);
            }
            return 0D;
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
        dto.setStatus(apartment.getStatus().getValue());
    }

    @BeanMapping(qualifiedByName = "toApartmentPreviewDto", ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Named("toApartmentPreviewDtoList")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "area", target = "area")
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
        dto.setStatus(apartment.getStatus().getValue());
        dto.setCategoryName(apartment.getCategoryName());
        dto.setPhotos(getFiles(apartment.getPhotos()));
        dto.setIsHighlight(apartment.getHighlight());
        dto.setAuthor(getUserInfo(userRepository.findById(apartment.getAuthorId()).orElse(new User())));
        dto.setBedroomQuantity(Long.valueOf(apartment.getBedroomQuantity()));
        dto.setBathroomQuantity(Long.valueOf(apartment.getBathroomQuantity()));
        dto.setFloorQuantity(Long.valueOf(apartment.getFloorQuantity()));
        dto.setAddress(districtRepository.findById(apartment.getDistrictId()).get().getName() + ", " + provinceRepository.findById(apartment.getProvinceId()).get().getName());
        dto.setTypeApartment(ETypeApartment.valueOf(apartment.getTypeApartment()).getValue());
        if (apartment.getTypeApartment().equals(ETypeApartment.BUY.name())) {
            dto.setTotalPrice(StringUtils.castPriceFromNumber(apartment.getTotal_Price()));
        } else {
            if (apartment.getUnit_Rent() == null) {
                dto.setTotalPrice("0");
            }
            dto.setTotalPrice(apartment.getUnit_Rent().trim());
        }

        if (userId != null && favouriteRepository.findByApartmentIdAndUserId(apartment.getId(), userId).isPresent()) {
            dto.setFavourite(true);
        }
        dto.setCreatedAt(apartment.getCreatedAt());
        dto.setPercentSuitable(calculateSuitableWithIpAndUserId(apartment.getRating(), ip, userId));
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
    protected void toApartmentFullDto(Apartment apartment, @MappingTarget ApartmentDto dto, @Context Long userId, @Context String ip) {
        dto.setAddress(apartment.getApartmentAddress().getDistrict().getName()
                + ", " + apartment.getApartmentAddress().getProvince().getName());
        dto.setApartmentDetail(apartmentDetailMapper.toApartmentDetailDto(apartment.getApartmentDetail()));
        dto.setAddressDetail(apartmentAddressMapper.toApartmentAddressDto(apartment.getApartmentAddress()));
        if (userId != null && favouriteRepository.findByApartmentIdAndUserId(apartment.getId(), userId).isPresent()) {
            dto.setFavourite(true);
        }
        dto.setTypeApartment(apartment.getTypeApartment().getValue());
        dto.setTotalPrice(convertPriceToString(apartment));
        dto.setPercentSuitable(calculateSuitableWithIpAndUserId(apartment.getId(), ip, userId));
        dto.setStatus(apartment.getStatus().getValue());
    }

    @BeanMapping(qualifiedByName = "toApartmentFullDto", ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "area", target = "area")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "photos", target = "photos", qualifiedByName = "getFiles")
    @Mapping(source = "author", target = "author", qualifiedByName = "getUserInfo")
    public abstract ApartmentDto toApartmentFullDto(Apartment apartment, @Context Long userId, @Context String ip);


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
        dto.setStatus(apartment.getStatus().getValue());
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
    @Mapping(source = "photos", target = "photos", qualifiedByName = "getFiles")
    @Mapping(source = "author", target = "author", qualifiedByName = "getUserInfo")
    public abstract ApartmentBasicDto toApartmentBasicDto(Apartment apartment, @Context Long userId, @Context String ip);

    @BeanMapping(ignoreByDefault = true)
    public abstract List<ApartmentBasicDto> toApartmentBasicDtoList(List<Apartment> apartmentList, @Context Long userId, @Context String ip);

    @Named("toApartmentRatingBasicDto")
    @BeforeMapping
    protected void toApartmentRatingBasicDto(ApartmentRating apartment, @MappingTarget ApartmentBasicDto dto, @Context Long userId, @Context String ip) {
        dto.setId(apartment.getId());
        dto.setTitle(apartment.getTitle());
        dto.setArea(apartment.getArea());
        dto.setStatus(apartment.getStatus().getValue());
        dto.setCategoryName(apartment.getCategoryName());
        dto.setPhotos(getFiles(apartment.getPhotos()));
        dto.setAuthor(getUserInfo(userRepository.findById(apartment.getAuthorId()).orElse(new User())));
        dto.setBedroomQuantity(Long.valueOf(apartment.getBedroomQuantity()));
        dto.setBathroomQuantity(Long.valueOf(apartment.getBathroomQuantity()));
        dto.setAddress(districtRepository.findById(apartment.getDistrictId()).get().getName() + ", " + provinceRepository.findById(apartment.getProvinceId()).get().getName());
        dto.setTypeApartment(ETypeApartment.valueOf(apartment.getTypeApartment()).getValue());
        if (apartment.getTypeApartment().equals(ETypeApartment.BUY.name())) {
            dto.setTotalPrice(StringUtils.castPriceFromNumber(apartment.getTotal_Price()));
        } else {
            if (apartment.getUnit_Rent() == null) {
                dto.setTotalPrice("0");
            }
            dto.setTotalPrice(apartment.getUnit_Rent().trim());
        }

        if (userId != null && favouriteRepository.findByApartmentIdAndUserId(apartment.getId(), userId).isPresent()) {
            dto.setFavourite(true);
        }
        dto.setCreatedAt(apartment.getCreatedAt());
        dto.setPercentSuitable(calculateSuitableWithIpAndUserId(apartment.getRating(), ip, userId));
    }

    @BeanMapping(qualifiedByName = "toApartmentRatingBasicDto", ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "createdAt", target = "createdAt")
    public abstract ApartmentBasicDto toApartmentRatingBasicDto(ApartmentRating apartment, @Context Long userId, @Context String ip);

    @BeanMapping(ignoreByDefault = true)
    public abstract List<ApartmentBasicDto> toApartmentBasicRatingDtoList(List<ApartmentRating> apartmentList, @Context Long userId, @Context String ip);

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
            apartment.setPriceRent(addApartmentRequest.getTotalPrice());
            apartment.setUnitRent(addApartmentRequest.getTotalPrice().toString() + " / tháng");
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
    protected void toApartmentCompareDto(Apartment apartment, @MappingTarget ApartmentCompareDto apartmentCompareDto, @Context Long userId, @Context String ip) {
        apartmentCompareDto.setAddress(apartment.getApartmentAddress().getAddress() + ", "
                + apartment.getApartmentAddress().getDistrict().getName() + ", "
                + apartment.getApartmentAddress().getProvince().getName() + ", "
                + apartment.getApartmentAddress().getCountry().getName());
        apartmentCompareDto.setTotalPrice(convertPriceToString(apartment));
        apartmentCompareDto.setPercentSuitable(calculateSuitableWithIpAndUserId(apartment.getId(), ip, userId));
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
    public abstract ApartmentCompareDto toApartmentCompareDto(Apartment apartment, @Context Long userId, @Context String ip);

    @BeanMapping(ignoreByDefault = true)
    public abstract List<ApartmentCompareDto> toApartmentCompareDtoList(List<Apartment> apartmentList, @Context Long userId, @Context String ip);

}
