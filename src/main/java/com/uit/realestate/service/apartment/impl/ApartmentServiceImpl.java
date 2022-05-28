package com.uit.realestate.service.apartment.impl;

import com.uit.realestate.constant.AppConstant;
import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.constant.SuitabilityConstant;
import com.uit.realestate.constant.enums.ETrackingType;
import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.constant.enums.user.ERoleType;
import com.uit.realestate.domain.TrackingTemporaryChat;
import com.uit.realestate.domain.action.Favourite;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.domain.apartment.ApartmentRating;
import com.uit.realestate.domain.user.User;
import com.uit.realestate.dto.apartment.*;
import com.uit.realestate.dto.response.PaginationResponse;
import com.uit.realestate.exception.InvalidException;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.mapper.TrackingChatbotMapper;
import com.uit.realestate.mapper.apartment.ApartmentMapper;
import com.uit.realestate.payload.CatchInfoRequestExt;
import com.uit.realestate.payload.apartment.*;
import com.uit.realestate.repository.action.FavouriteRepository;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.repository.apartment.spec.ApartmentSpecification;
import com.uit.realestate.repository.category.CategoryRepository;
import com.uit.realestate.repository.tracking.TrackingTemporaryChatRepository;
import com.uit.realestate.repository.user.UserRepository;
import com.uit.realestate.service.AsyncService;
import com.uit.realestate.service.apartment.ApartmentService;
import com.uit.realestate.service.location.DistrictService;
import com.uit.realestate.service.tracking.TrackingService;
import com.uit.realestate.utils.MessageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ApartmentServiceImpl implements ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final ApartmentMapper apartmentMapper;
    private final TrackingChatbotMapper trackingChatbotMapper;

    private final DistrictService districtService;
    private final TrackingService trackingService;

    private final MessageHelper messageHelper;

    private final UserRepository userRepository;
    private final FavouriteRepository favouriteRepository;
    private final CategoryRepository categoryRepository;
    private final TrackingTemporaryChatRepository trackingTemporaryChatRepository;
    private final AsyncService asyncService;

    @Override
    public boolean addApartment(AddApartmentRequest req) {
        if (Objects.nonNull(req.getApartmentAddress())) {
            districtService.validationDistrict(req.getApartmentAddress().getDistrictId(), req.getApartmentAddress().getProvinceId());
        }
        if (req.getTypeApartment().equals(ETypeApartment.BUY)) {
            if (req.getTotalPrice() == null) {
                throw new InvalidException(messageHelper.getMessage(MessageCode.ERROR, "Invalid total price"));
            }
        } else {
            if (req.getPriceRent() == null || req.getUnitRent() == null) {
                throw new InvalidException(messageHelper.getMessage(MessageCode.ERROR, "Invalid Price Rent"));
            }
        }
        log.info("Add a new Apartment");
        apartmentRepository.save(apartmentMapper.toApartment(req));

        return true;
    }

    @Override
    public boolean closeApartmentService(Long apartmentId) {
        log.info("Close Apartment");
        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.Apartment.NOT_FOUND)));
        apartment.setStatus(EApartmentStatus.CLOSE);

        apartmentRepository.save(apartment);

        return true;
    }

    @Override
    public List<ApartmentCompareDto> compareApartment(CompareApartmentRequest req) {
        req.getIds().forEach(item -> {
            if (!apartmentRepository.existsById(item)) {
                throw new NotFoundException(messageHelper.getMessage(MessageCode.Apartment.NOT_FOUND));
            }
        });

        log.info("Compare Apartment");
        return apartmentMapper.toApartmentCompareDtoList(apartmentRepository.findAllByIdIn(req.getIds()), req.getUserId());
    }

    @Override
    public boolean favouriteApartment(FavouriteApartmentRequest req) {
        Apartment apartment = apartmentRepository.findById(req.getApartmentId())
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.Apartment.NOT_FOUND)));
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.User.NOT_FOUND)));
        Favourite favourite = favouriteRepository.findByApartmentIdAndUserId(req.getApartmentId(), req.getUserId())
                .orElse(null);

        Long rating;
        Map<ETrackingType, String> map = new HashMap<>();
        map.put(ETrackingType.CATEGORY, String.valueOf(apartment.getCategory().getId()));
        map.put(ETrackingType.DISTRICT, String.valueOf(apartment.getApartmentAddress().getDistrict().getId()));
        map.put(ETrackingType.PROVINCE, String.valueOf(apartment.getApartmentAddress().getProvince().getId()));
        map.put(ETrackingType.TYPE, apartment.getTypeApartment().name());
        map.put(ETrackingType.AREA, String.valueOf(apartment.getArea()));
        map.put(ETrackingType.BATHROOM, String.valueOf(apartment.getApartmentDetail().getBathroomQuantity()));
        map.put(ETrackingType.BEDROOM, String.valueOf(apartment.getApartmentDetail().getBedroomQuantity()));
        map.put(ETrackingType.DIRECTION, apartment.getApartmentDetail().getHouseDirection());
        map.put(ETrackingType.FLOOR, String.valueOf(apartment.getApartmentDetail().getFloorQuantity()));
        map.put(ETrackingType.PRICE, String.valueOf(apartment.getTypeApartment().equals(ETypeApartment.BUY) ? apartment.getTotalPrice() : apartment.getPriceRent()));
        map.put(ETrackingType.TOILET, String.valueOf(apartment.getApartmentDetail().getToiletQuantity()));

        if (favourite == null) {
            log.info("Favourite Apartment with apartment ID: " + req.getApartmentId());

            user.addFavourite(new Favourite(apartment, user));

            rating = AppConstant.DEFAULT_FAVOURITE_RATING;
        } else {
            log.info("Disable favourite Apartment  with apartment ID: " + req.getApartmentId());

            user.removeFavourite(favourite);

            rating = AppConstant.DEFAULT_DISABLE_FAVOURITE_RATING;
        }
        trackingService.tracking(req.getUserId(), req.getIp(), map, rating);
        userRepository.save(user);
        return true;
    }

    @Override
    public List<ApartmentBasicDto> findHighLightApartment(HighlightApartmentRequest req) {
        log.info("Find top highlight apartment");
        return apartmentMapper.toApartmentBasicDtoList(apartmentRepository
                .findAllByHighlightTrueAndStatusAndApartmentAddressProvinceIdOrderByUpdatedAtDesc(EApartmentStatus.OPEN, req.getProvinceId()), req.getUserId(), req.getIp());
    }

    @Override
    public List<ApartmentBasicDto> findLatestApartment(LatestApartmentRequest req) {
        log.info("Find top 16 new latest apartment");
        return apartmentMapper.toApartmentBasicDtoList(apartmentRepository
                .findTop16ByStatusAndTypeApartmentOrderByCreatedAtDesc(EApartmentStatus.OPEN, req.getTypeApartment()), req.getUserId(), req.getIp());
    }

    @Override
    public PaginationResponse<ApartmentBasicDto> findRecommendApartment(RecommendApartmentRequest req) {
        log.info("Find recommend apartment");
        Page<Apartment> result = apartmentRepository
                .findRecommendApartmentByUserIdAndIp(req.getTypeApartment().name(), req.getUserId(), req.getIp(), req.getPageable());
        return new PaginationResponse<>(
                result.getTotalElements()
                , result.getNumberOfElements()
                , result.getNumber() + 1
                , apartmentMapper.toApartmentBasicDtoList(result.getContent(), req.getUserId(), req.getIp()));
    }

    @Override
    public PaginationResponse<ApartmentBasicDto> findSimilarApartment(CatchInfoRequestExt req) {
        log.info("Find Similar apartment");
        Page<Apartment> result = apartmentRepository
                .findRecommendApartmentByUserIdAndIp(ETypeApartment.BUY.name(), req.getUserId(), req.getIp(), req.getPageable());
        List<ApartmentBasicDto> contents = apartmentMapper.toApartmentBasicDtoList(result.getContent(), req.getUserId(), req.getIp());
        Collections.shuffle(contents);
        return new PaginationResponse<>(
                result.getTotalElements()
                , result.getNumberOfElements()
                , result.getNumber() + 1
                , contents);
    }

    @Override
    public ApartmentDto getApartmentDetail(DetailApartmentRequest req) {
        //Validation
        Apartment apartment = apartmentRepository.findById(req.getId())
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.Apartment.NOT_FOUND)));

        if (apartment.getStatus() != EApartmentStatus.OPEN) {
            if (req.getUserId() == null) {
                throw new NotFoundException(messageHelper.getMessage(MessageCode.Apartment.NOT_FOUND));
            } else {
                User user = userRepository.findById(req.getUserId()).orElseThrow(() ->
                        new NotFoundException(messageHelper.getMessage(MessageCode.User.NOT_FOUND)));
                if (apartment.getAuthor() != user && !user.getRole().getId().equals(ERoleType.ADMIN)) {
                    throw new NotFoundException(messageHelper.getMessage(MessageCode.Apartment.NOT_FOUND));
                }
            }
        }
        ApartmentDto result = apartmentMapper.toApartmentFullDto(apartment, req.getUserId());

        log.info("Tracking User");
        Map<ETrackingType, String> map = new HashMap<>();
        map.put(ETrackingType.CATEGORY, String.valueOf(apartment.getCategory().getId()));
        map.put(ETrackingType.DISTRICT, String.valueOf(apartment.getApartmentAddress().getDistrict().getId()));
        map.put(ETrackingType.PROVINCE, String.valueOf(apartment.getApartmentAddress().getProvince().getId()));
        map.put(ETrackingType.TYPE, apartment.getTypeApartment().name());
        map.put(ETrackingType.AREA, String.valueOf(apartment.getArea()));
        map.put(ETrackingType.BATHROOM, String.valueOf(apartment.getApartmentDetail().getBathroomQuantity()));
        map.put(ETrackingType.BEDROOM, String.valueOf(apartment.getApartmentDetail().getBedroomQuantity()));
        map.put(ETrackingType.DIRECTION, apartment.getApartmentDetail().getHouseDirection());
        map.put(ETrackingType.FLOOR, String.valueOf(apartment.getApartmentDetail().getFloorQuantity()));
        map.put(ETrackingType.PRICE, String.valueOf(apartment.getTypeApartment().equals(ETypeApartment.BUY) ? apartment.getTotalPrice() : apartment.getPriceRent()));
        map.put(ETrackingType.TOILET, String.valueOf(apartment.getApartmentDetail().getToiletQuantity()));

        trackingService.tracking(req.getUserId(), req.getIp(), map, AppConstant.DEFAULT_RATING);

        log.info("Get detail apartment ID: " + req.getId());

        return result;
    }

    @Override
    public boolean highLightApartment(Long apartmentId) {
        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.Apartment.NOT_FOUND)));
        if (apartment.getHighlight()) {
            log.info("UnHighlight apartment with ID:" + apartmentId);
            apartment.setHighlight(false);
        } else {
            log.info("Highlight apartment with ID:" + apartmentId);
            apartment.setHighlight(true);
        }

        apartmentRepository.save(apartment);

        return true;
    }

    @Override
    public List<ApartmentSearchDto> searchAllApartment(String req) {
        log.info("Search All Apartment");

        List<Apartment> result = apartmentRepository.findAllByStatusAndTitleContainingIgnoreCase(EApartmentStatus.OPEN, req);

        return apartmentMapper.toApartmentSearchDtoList(result);
    }

    @Override
    public PaginationResponse<ApartmentDto> searchApartment(SearchApartmentRequest req) {
        log.info("Search Apartment");
//        Page<ApartmentRating> result = apartmentRepository.findRecommendApartmentByUserIdAndIp(req, req.getUserId(), req.getIp(), req.getPageable());
        Page<ApartmentRating> result = apartmentRepository.test2(req, req.getUserId(), req.getIp(), req.getPageable());

        return new PaginationResponse<>(
                result.getTotalElements()
                , result.getNumberOfElements()
                , result.getNumber() + 1
                , apartmentMapper.toApartmentRatingPreviewDtoList(result.getContent(), req.getUserId(), req.getIp()));
    }

    @Override
    public boolean updateApartment(UpdateApartmentRequest req) {
        // Validation
        Apartment apartment = apartmentRepository.findById(req.getId())
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.Apartment.NOT_FOUND)));
        if (!req.getIsAdmin() && !req.getAuthorId().equals(apartment.getAuthor().getId())) {
            throw new InvalidException(messageHelper.getMessage(MessageCode.Token.NOT_PERMISSION));
        }
        if (!req.getIsAdmin() && !apartment.getStatus().equals(EApartmentStatus.PENDING)) {
            throw new NotFoundException(messageHelper.getMessage(MessageCode.Token.NOT_PERMISSION));
        }
        if (req.getCategoryId() != null && categoryRepository.findById(req.getCategoryId()).isEmpty()) {
            throw new NotFoundException(messageHelper.getMessage(MessageCode.Category.NOT_FOUND));
        }
        if (req.getApartmentAddress() != null) {
            districtService.validationDistrict(req.getApartmentAddress().getDistrictId(), req.getApartmentAddress().getProvinceId());
        }

        if (req.getTypeApartment().equals(ETypeApartment.BUY)) {
            if (req.getTotalPrice() == null) {
                throw new InvalidException(messageHelper.getMessage(MessageCode.ERROR, "Invalid total price"));
            }
        } else {
            if (req.getPriceRent() == null || req.getUnitRent() == null) {
                throw new InvalidException(messageHelper.getMessage(MessageCode.ERROR, "Invalid Price Rent"));
            }
        }

        log.info("Update apartment");
        apartmentMapper.updateApartment(req, apartment);
        apartmentRepository.save(apartment);
        return true;
    }

    @Override
    public boolean validateApartment(ValidateApartmentRequest req) {
        log.info("Validate Apartment");

        var apartment = apartmentRepository.findById(req.getId())
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.Apartment.NOT_FOUND)));
        apartment.setStatus(req.getDecision() ? EApartmentStatus.OPEN : EApartmentStatus.CLOSE);

        return true;
    }

    @Override
    public boolean existApartment(String title) {
        log.info("Validate Apartment with title");
        if (title == null || title.equals("")) {
            throw new NotFoundException("Title Null");
        }
        var apartment = apartmentRepository.findAllByTitleContainingIgnoreCase(title);

        return apartment.size() > 0;
    }

    @Override
    public Long findByTitleNewest(String title) {
        var apartment = apartmentRepository.findAllByTitleContainingIgnoreCaseOrderByCreatedAtDesc(title);

        if (apartment.size() > 0) {
            return apartment.stream().findFirst().get().getId();
        }
        return null;
    }

    @Override
    public void deletePermanent(String title) {
        var apartment = apartmentRepository.findAllByTitleContainingIgnoreCaseOrderByCreatedAtDesc(title);

        if (apartment.size() > 0) {
            apartmentRepository.delete(apartment.stream().findFirst().get());
        }
    }

    @Override
    public PaginationResponse<ApartmentBasicDto> findApartmentWithSuitable(Long userId) {
        List<Apartment> result = apartmentRepository
                .findApartmentWithSuitableDesc(SuitabilityConstant.DEFAULT_ACCURACY,
                        SuitabilityConstant.DEFAULT_ACCURACY_AREA, 2L, 1L, 1L);
        return null;
    }

    @Override
    @Async
    public void findAndSaveRecommendApartmentForChatBox(ApartmentQueryParam req, String key) {
        try {
            TrackingTemporaryChat trackingTemporaryChat = trackingTemporaryChatRepository
                    .findByKey(key).orElse(null);
            if (trackingTemporaryChat == null) {
                trackingTemporaryChat = new TrackingTemporaryChat();
                trackingTemporaryChat.setKey(key);
                trackingTemporaryChatRepository.saveAndFlush(trackingTemporaryChat);
            }
            if (Objects.nonNull(req.getUserId())) {
                asyncService.findAndSaveWithUserTarget(req, key);
            }
            asyncService.findAndSaveWithUserOrIp(req, key);
            asyncService.findAndSaveWithLatestRandom(req, key);
        } catch (Exception e) {
            e.printStackTrace();
            asyncService.removeKey(key);
        }
    }

    @Override
    public List<ThumbnailChatDto> findApartmentForChat(String key, Long userId) {
        TrackingTemporaryChat trackingTemporaryChat = trackingTemporaryChatRepository.findByKey(key).orElse(null);
        List<Long> ids;
        List<Long> listTemp;
        if (Objects.isNull(trackingTemporaryChat)) {
            return new ArrayList<>();
        }
        if (Objects.isNull(userId) || userId.equals(-1L)) {
            listTemp = truncateListIds(trackingTemporaryChat.getListRecommendWithUserOrIp());
            if (Objects.isNull(listTemp)) {
                ids = truncateListIds(trackingTemporaryChat.getListRecommendWithLatestRandom());
                if (!(Objects.isNull(ids) || ids.isEmpty())) {
                    System.out.println("Bot with: No User - recommend with random");
                }
            } else {
                ids = listTemp;
                if (!ids.isEmpty()) {
                    System.out.println("Bot with: No User - recommend with user or ip");
                }
            }
        } else {
            listTemp = truncateListIds(trackingTemporaryChat.getListRecommendWithUserTargets());
            if (Objects.isNull(listTemp)) {
                listTemp = truncateListIds(trackingTemporaryChat.getListRecommendWithUserOrIp());
                if (Objects.isNull(listTemp)) {
                    ids = truncateListIds(trackingTemporaryChat.getListRecommendWithLatestRandom());
                    if (!(Objects.isNull(ids) || ids.isEmpty())) {
                        System.out.println("Bot with: Had User - recommend with random");
                    }
                } else {
                    ids = listTemp;
                    if (!ids.isEmpty()) {
                        System.out.println("Bot with: Had User - recommend with user or ip");
                    }
                }
            } else {
                ids = listTemp;
                if (!ids.isEmpty()) {
                    System.out.println("Bot with: Had User - recommend with User Target");
                }
            }
        }
        if (Objects.isNull(ids) || ids.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            List<Apartment> apartments = apartmentRepository.findAllByStatusAndIdIn(EApartmentStatus.OPEN, ids);
            if (apartments.isEmpty()) {
                return new ArrayList<>();
            }
            return trackingChatbotMapper.toThumbnailChatDtoList(apartments.stream().filter(apartment -> !apartment.getPhotos().equals("[]")).collect(Collectors.toList()));
        } finally {
            trackingTemporaryChatRepository.delete(trackingTemporaryChat);
            trackingTemporaryChatRepository.flush();
        }
    }

    private List<Long> truncateListIds(List<Long> list) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        if (list.contains(-1L)) {
            return null;
        }
        return list;
    }
}
