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
import com.uit.realestate.domain.user.User;
import com.uit.realestate.dto.apartment.*;
import com.uit.realestate.dto.response.PaginationResponse;
import com.uit.realestate.exception.InvalidException;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.mapper.apartment.ApartmentMapper;
import com.uit.realestate.payload.CatchInfoRequest;
import com.uit.realestate.payload.CatchInfoRequestExt;
import com.uit.realestate.payload.apartment.*;
import com.uit.realestate.repository.action.FavouriteRepository;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.repository.apartment.spec.ApartmentSpecification;
import com.uit.realestate.repository.category.CategoryRepository;
import com.uit.realestate.repository.tracking.TrackingTemporaryChatRepository;
import com.uit.realestate.repository.user.UserRepository;
import com.uit.realestate.service.apartment.ApartmentService;
import com.uit.realestate.service.location.DistrictService;
import com.uit.realestate.service.tracking.TrackingService;
import com.uit.realestate.utils.MessageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.NotTransactional;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ApartmentServiceImpl implements ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final ApartmentMapper apartmentMapper;

    private final DistrictService districtService;
    private final TrackingService trackingService;

    private final MessageHelper messageHelper;

    private final UserRepository userRepository;
    private final FavouriteRepository favouriteRepository;
    private final CategoryRepository categoryRepository;
    private final TrackingTemporaryChatRepository trackingTemporaryChatRepository;

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
        Map<ETrackingType, Long> map = new HashMap<>();
        map.put(ETrackingType.CATEGORY, apartment.getCategory().getId());
        map.put(ETrackingType.DISTRICT, apartment.getApartmentAddress().getDistrict().getId());
        map.put(ETrackingType.PROVINCE, apartment.getApartmentAddress().getProvince().getId());

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
    public List<ApartmentBasicDto> findHighLightApartment(CatchInfoRequest req) {
        log.info("Find top 4 highlight apartment");
        return apartmentMapper.toApartmentBasicDtoList(apartmentRepository
                .findTop4ByHighlightTrueAndStatusOrderByUpdatedAtDesc(EApartmentStatus.OPEN), req.getUserId());
    }

    @Override
    public List<ApartmentBasicDto> findLatestApartment(CatchInfoRequest req) {
        log.info("Find top 4 new latest apartment");
        return apartmentMapper.toApartmentBasicDtoList(apartmentRepository
                .findTop4ByStatusOrderByCreatedAtDesc(EApartmentStatus.OPEN), req.getUserId());
    }

    @Override
    public PaginationResponse<ApartmentBasicDto> findRecommendApartment(CatchInfoRequestExt req) {
        log.info("Find recommend apartment");
        Page<Apartment> result = apartmentRepository
                .findRecommendApartmentByUserIdAndIp(req.getUserId(), req.getIp(), req.getPageable());
        return new PaginationResponse<>(
                result.getTotalElements()
                , result.getNumberOfElements()
                , result.getNumber() + 1
                , apartmentMapper.toApartmentBasicDtoList(result.getContent(), req.getUserId()));
    }

    @Override
    public PaginationResponse<ApartmentBasicDto> findSimilarApartment(CatchInfoRequestExt req) {
        log.info("Find Similar apartment");
        Page<Apartment> result = apartmentRepository
                .findRecommendApartmentByUserIdAndIp(req.getUserId(), req.getIp(), req.getPageable());
        List<ApartmentBasicDto> contents = apartmentMapper.toApartmentBasicDtoList(result.getContent(), req.getUserId());
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
        Map<ETrackingType, Long> map = new HashMap<>();
        map.put(ETrackingType.CATEGORY, apartment.getCategory().getId());
        map.put(ETrackingType.DISTRICT, apartment.getApartmentAddress().getDistrict().getId());
        map.put(ETrackingType.PROVINCE, apartment.getApartmentAddress().getProvince().getId());
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
        req.setApartmentStatus(EApartmentStatus.OPEN);
        Page<Apartment> result = apartmentRepository.findAll(ApartmentSpecification.of(req), req.getPageable());

        return new PaginationResponse<>(
                result.getTotalElements()
                , result.getNumberOfElements()
                , result.getNumber() + 1
                , apartmentMapper.toApartmentPreviewDtoList(result.getContent(), req.getUserId()));
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
        List<Apartment> result;
        if (req.getUserId() == null || req.getUserId() == -1){
            result = apartmentRepository
                    .findRecommendApartmentForChatBox(req, req.getUserId(), req.getIp(), req.getPageable()).getContent();
        }else{
            result = apartmentRepository
                    .findSuitableApartmentForChatBox(req,SuitabilityConstant.DEFAULT_ACCURACY,
                            SuitabilityConstant.DEFAULT_ACCURACY_AREA, req.getUserId(), req.getPage(), req.getSize());
        }
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!trackingTemporaryChatRepository.existsByKey(req.generateKey())){
            TrackingTemporaryChat chat = new TrackingTemporaryChat();
            chat.setKey(key);
            chat.setValue(result.stream().map(Apartment::getId).collect(Collectors.toList()));
            trackingTemporaryChatRepository.saveAndFlush(chat);
        }
    }

    @Override
    public List<ThumbnailChatDto> findApartmentForChat(String key) {
        TrackingTemporaryChat trackingTemporaryChat = trackingTemporaryChatRepository.findByKey(key).orElse(null);
        if (Objects.isNull(trackingTemporaryChat)){
            return new ArrayList<>();
        }
        try{
            List<Apartment> apartments = apartmentRepository.findAllByStatusAndIdIn(EApartmentStatus.OPEN, trackingTemporaryChat.getValue());
            if (apartments.isEmpty()){
                return new ArrayList<>();
            }
            return List.of(new ThumbnailChatDto());
        }finally {
            trackingTemporaryChatRepository.delete(trackingTemporaryChat);
        }

    }
}
