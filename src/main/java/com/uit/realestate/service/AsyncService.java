package com.uit.realestate.service;

import com.google.common.base.Strings;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.domain.TrackingTemporaryChat;
import com.uit.realestate.domain.apartment.ApartmentRating;
import com.uit.realestate.payload.TrackingTemporaryRequest;
import com.uit.realestate.payload.apartment.ApartmentQueryParam;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.repository.tracking.TrackingTemporaryChatRepository;
import com.uit.realestate.service.category.CategoryService;
import com.uit.realestate.service.location.DistrictService;
import com.uit.realestate.service.location.ProvinceService;
import com.uit.realestate.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AsyncService {

    private final long KEY_EMPTY_ID = -1L;

    private final ApartmentRepository apartmentRepository;
    private final TrackingTemporaryChatRepository trackingTemporaryChatRepository;
    private final CategoryService categoryService;
    private final DistrictService districtService;
    private final ProvinceService provinceService;

    @Async
    public void findAndSaveWithUserOrIp(ApartmentQueryParam req, String key) {
        try {
            List<ApartmentRating> result = apartmentRepository
                    .findSuitableApartmentForChatBox(req, req.getUserId(), req.getIp(), req.getPage() -1, req.getSize());

            List<Long> ids = result.stream().map(ApartmentRating::getId).collect(Collectors.toList());
            if (ids.isEmpty()) {
                ids.add(KEY_EMPTY_ID);
            }
            saveTrackingInfo(TrackingTemporaryRequest.builder().key(key).value(ids).build());
        } catch (Exception e) {
            e.printStackTrace();
            removeKey(key);
        }
    }

    private void saveTrackingInfo(TrackingTemporaryRequest request) {
        try {
            TrackingTemporaryChat trackingTemporaryChat = trackingTemporaryChatRepository
                    .findByKey(request.getKey()).orElse(new TrackingTemporaryChat());
            if (Objects.isNull(trackingTemporaryChat.getKey())) {
                trackingTemporaryChat.setKey(request.getKey());
            }
            if (Objects.nonNull(request.getValue())) {
                trackingTemporaryChat.setValue(request.getValue());
            }

            trackingTemporaryChatRepository.saveAndFlush(trackingTemporaryChat);
        } catch (Exception e) {
            e.printStackTrace();
            //ignore exception
        }

    }

    public void removeKey(@NonNull String key) {
        trackingTemporaryChatRepository
                .findByKey(key).ifPresent(trackingTemporaryChatRepository::delete);
    }

    public String generateTitle(@NonNull ApartmentQueryParam param) {
        StringBuilder builder = new StringBuilder();

        if (!Strings.isNullOrEmpty(param.getType())) {
            builder.append("Thể Loại ").append(ETypeApartment.valueOf(param.getType()).getDisplayName());
        }

        if (Objects.nonNull(param.getAreaLow()) && Objects.nonNull(param.getAreaHigh()) && Objects.isNull(param.getAreaRange())) {
            builder = appendSeparate(builder);
            builder.append("Diện tích ").append(StringUtils.castPriceFromNumber((double)Math.round(param.getAreaLow()))).append("m2")
                    .append(" - ").append(StringUtils.castPriceFromNumber((double)Math.round(param.getAreaHigh()))).append("m2");
        } else {
            if (Objects.nonNull(param.getAreaRange())) {
                builder = appendSeparate(builder);
                builder.append("Diện tích khoảng ").append(StringUtils.castPriceFromNumber((double)Math.round(param.getAreaRange()))).append("m2");
            }
        }

        if (Objects.nonNull(param.getPriceLow()) && Objects.nonNull(param.getPriceHigh()) && Objects.isNull(param.getPriceRange())) {
            builder = appendSeparate(builder);
            builder.append("Giá ").append(StringUtils.castPriceFromNumber((double)Math.round(param.getPriceLow())))
                    .append(" - ").append(StringUtils.castPriceFromNumber((double)Math.round(param.getPriceHigh())));
        } else {
            if (Objects.nonNull(param.getPriceRange())) {
                builder = appendSeparate(builder);
                builder.append("Giá khoảng ").append(StringUtils.castPriceFromNumber((double)Math.round(param.getPriceRange())));
            }
        }

        if (!param.getBathrooms().isEmpty()) {
            builder = appendSeparate(builder);
            builder.append("SL phòng tắm ").append(param.getBathrooms().stream().map(Object::toString)
                    .collect(Collectors.joining("-")));
        }

        if (!param.getBedrooms().isEmpty()) {
            builder = appendSeparate(builder);
            builder.append("SL phòng ngủ ").append(param.getBedrooms().stream().map(Object::toString)
                    .collect(Collectors.joining("-")));
        }

        if (!param.getFloors().isEmpty()) {
            builder = appendSeparate(builder);
            builder.append("SL tầng ").append(param.getFloors().stream().map(Object::toString)
                    .collect(Collectors.joining("-")));
        }

        if (!param.getCategories().isEmpty()) {
            builder = appendSeparate(builder);
            builder.append("Thể loại ").append(param.getCategories().stream().map(item -> categoryService.findCategory(item).getName())
                    .filter(item -> !item.equals("")).collect(Collectors.joining("-")));
        }

        if (!param.getDistricts().isEmpty() || !param.getProvinces().isEmpty()){
            builder = appendSeparate(builder);
            builder.append("Tại");
        }
        if (!param.getDistricts().isEmpty()) {
            builder.append(" ").append(param.getDistricts().stream().map(item -> districtService.findById(item).getName())
                    .filter(item -> !item.equals("")).collect(Collectors.joining("-")));
        }

        if (!param.getProvinces().isEmpty()) {
            builder.append(" ").append(param.getProvinces().stream().map(item -> provinceService.findById(item).getName())
                    .filter(item -> !item.equals("")).collect(Collectors.joining("-")));
        }

        return "(" + builder + ")";
    }

    private StringBuilder appendSeparate(StringBuilder builder) {
        if (builder.length() == 0) {
            return builder;
        }
        return builder.append(", ");
    }
}
