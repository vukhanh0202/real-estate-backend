package com.uit.realestate.service;

import com.uit.realestate.constant.SuitabilityConstant;
import com.uit.realestate.domain.TrackingTemporaryChat;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.payload.TrackingTemporaryRequest;
import com.uit.realestate.payload.apartment.ApartmentQueryParam;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.repository.apartment.spec.ApartmentSpecification;
import com.uit.realestate.repository.tracking.TrackingTemporaryChatRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.PageRequest;
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

    @Async
    public void findAndSaveWithUserTarget(ApartmentQueryParam req, @NonNull String key) {
        List<Apartment> result = apartmentRepository
                .findSuitableApartmentForChatBox(req, SuitabilityConstant.DEFAULT_ACCURACY,
                        SuitabilityConstant.DEFAULT_ACCURACY_AREA, req.getUserId(), req.getPage(), req.getSize());
        List<Long> ids = result.stream().map(Apartment::getId).collect(Collectors.toList());
        if(ids.isEmpty()){
            ids.add(KEY_EMPTY_ID);
        }
        saveTrackingInfo(TrackingTemporaryRequest.builder().key(key).listRecommendWithUserTargets(ids).build());
    }

    @Async
    public void findAndSaveWithUserOrIp(ApartmentQueryParam req, String key) {
        List<Apartment> result = apartmentRepository
                .findRecommendApartmentForChatBox(req, req.getUserId(), req.getIp(), req.getPageable()).getContent();

        List<Long> ids = result.stream().map(Apartment::getId).collect(Collectors.toList());
        if(ids.isEmpty()){
            ids.add(KEY_EMPTY_ID);
        }
        saveTrackingInfo(TrackingTemporaryRequest.builder().key(key).listRecommendWithUserOrIp(ids).build());
    }

    @Async
    public void findAndSaveWithLatestRandom(ApartmentQueryParam req, String key) {
        List<Apartment> result = apartmentRepository.findAll(ApartmentSpecification.of(req), PageRequest.of(0, 5)).getContent();
        List<Long> ids = result.stream().map(Apartment::getId).collect(Collectors.toList());
        if(ids.isEmpty()){
            ids.add(KEY_EMPTY_ID);
        }
        saveTrackingInfo(TrackingTemporaryRequest.builder().key(key).listRecommendWithLatestRandom(ids).build());
    }

    private void saveTrackingInfo(TrackingTemporaryRequest request){
        TrackingTemporaryChat trackingTemporaryChat = trackingTemporaryChatRepository
                .findByKey(request.getKey()).orElse(new TrackingTemporaryChat());
        if (Objects.isNull(trackingTemporaryChat.getKey())){
            trackingTemporaryChat.setKey(request.getKey());
        }
        if (Objects.nonNull(request.getListRecommendWithUserTargets())){
            trackingTemporaryChat.setListRecommendWithUserTargets(request.getListRecommendWithUserTargets());
        }
        if (Objects.nonNull(request.getListRecommendWithUserOrIp())){
            trackingTemporaryChat.setListRecommendWithUserOrIp(request.getListRecommendWithUserOrIp());
        }
        if (Objects.nonNull(request.getListRecommendWithLatestRandom())){
            trackingTemporaryChat.setListRecommendWithLatestRandom(request.getListRecommendWithLatestRandom());
        }

        trackingTemporaryChatRepository.saveAndFlush(trackingTemporaryChat);
    }
}
