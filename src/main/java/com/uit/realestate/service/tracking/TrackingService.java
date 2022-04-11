package com.uit.realestate.service.tracking;

import com.uit.realestate.constant.enums.ETrackingType;
import com.uit.realestate.domain.tracking.TrackingCategory;
import com.uit.realestate.domain.tracking.TrackingDistrict;
import com.uit.realestate.domain.tracking.TrackingProvince;
import com.uit.realestate.repository.category.CategoryRepository;
import com.uit.realestate.repository.location.DistrictRepository;
import com.uit.realestate.repository.location.ProvinceRepository;
import com.uit.realestate.repository.tracking.TrackingCategoryRepository;
import com.uit.realestate.repository.tracking.TrackingDistrictRepository;
import com.uit.realestate.repository.tracking.TrackingProvinceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TrackingService {

    private final TrackingCategoryRepository trackingCategoryRepository;
    private final TrackingProvinceRepository trackingProvinceRepository;
    private final TrackingDistrictRepository trackingDistrictRepository;

    private final CategoryRepository categoryRepository;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;


    public void tracking(Long userId, String ip, Map<ETrackingType, Long> mapTargetId, Long rating) {
        try{
            for (ETrackingType trackingType : mapTargetId.keySet()) {
                switch (trackingType) {
                    case CATEGORY:
                        trackingCategory(userId, ip, mapTargetId.get(trackingType), rating);
                    case DISTRICT:
                        trackingDistrict(userId, ip, mapTargetId.get(trackingType), rating);
                    case PROVINCE:
                        trackingProvince(userId, ip, mapTargetId.get(trackingType), rating);
                }
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    public void trackingCategory(Long userId, String ip, Long targetId, Long rating) {
        if (targetId == null) return;

        log.info("Tracking category " + targetId + "with UserId:" + userId + "And IP" + ip);
        List<TrackingCategory> trackingCategories = trackingCategoryRepository
                .findAll(getTrackingCategorySpecification(ip, targetId));
        trackingCategories = trackingCategories.stream().filter(trackingCategory -> {
            if (userId == null) {
                return trackingCategory.getUser() == null;
            } else {
                return trackingCategory.getUser() != null && trackingCategory.getUser().getId().equals(userId);
            }
        }).collect(Collectors.toList());
        if (trackingCategories.isEmpty()) {
            trackingCategoryRepository.save(new TrackingCategory(userId, categoryRepository.findById(targetId).get(), ip, rating));
        } else {
            trackingCategories.forEach(trackingCategory -> {
                trackingCategory.setRating(trackingCategory.getRating() + rating);
            });
            trackingCategoryRepository.saveAll(trackingCategories);
        }
    }

    public void trackingDistrict(Long userId, String ip, Long targetId, Long rating) {
        if (targetId == null) return;

        log.info("Tracking District: " + targetId + "with UserId:" + userId + "And IP" + ip);

        List<TrackingDistrict> trackingDistricts = trackingDistrictRepository
                .findAll(getTrackingDistrictSpecification(ip, targetId));
        trackingDistricts = trackingDistricts.stream().filter(trackingDistrict -> {
            if (userId == null) {
                return trackingDistrict.getUser() == null;
            } else {
                return trackingDistrict.getUser() != null && trackingDistrict.getUser().getId().equals(userId);
            }
        }).collect(Collectors.toList());
        if (trackingDistricts.isEmpty()) {
            trackingDistrictRepository.save(new TrackingDistrict(userId, districtRepository.findById(targetId).get(), ip, rating));
        } else {
            trackingDistricts.forEach(trackingDistrict -> {
                trackingDistrict.setRating(trackingDistrict.getRating() + rating);
            });
            trackingDistrictRepository.saveAll(trackingDistricts);
        }
    }

    public void trackingProvince(Long userId, String ip, Long targetId, Long rating) {
        if (targetId == null) return;

        log.info("Tracking province: " + targetId + "with UserId:" + userId + "And IP" + ip);

        List<TrackingProvince> trackingProvinces = trackingProvinceRepository
                .findAll(getTrackingProvinceSpecification(ip, targetId));
        trackingProvinces = trackingProvinces.stream().filter(trackingProvince -> {
            if (userId == null) {
                return trackingProvince.getUser() == null;
            } else {
                return trackingProvince.getUser() != null && trackingProvince.getUser().getId().equals(userId);
            }
        }).collect(Collectors.toList());
        if (trackingProvinces.isEmpty()) {
            trackingProvinceRepository.save(new TrackingProvince(userId, provinceRepository.findById(targetId).get(), ip, rating));
        } else {
            trackingProvinces.forEach(trackingProvince -> {
                trackingProvince.setRating(trackingProvince.getRating() + rating);
            });
            trackingProvinceRepository.saveAll(trackingProvinces);
        }
    }

    private Specification<TrackingProvince> getTrackingProvinceSpecification(String ip, Long targetId) {
        return (Specification<TrackingProvince>) (root, query, builder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(builder.equal(root.get("province").get("id"), targetId));
            predicateList.add(builder.equal(root.get("ip"), ip));
            return builder.and(predicateList.toArray(new Predicate[0]));
        };
    }

    private Specification<TrackingDistrict> getTrackingDistrictSpecification(String ip, Long targetId) {
        return (Specification<TrackingDistrict>) (root, query, builder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(builder.equal(root.get("district").get("id"), targetId));
            predicateList.add(builder.equal(root.get("ip"), ip));
            return builder.and(predicateList.toArray(new Predicate[0]));
        };
    }

    private Specification<TrackingCategory> getTrackingCategorySpecification(String ip, Long targetId) {
        return (Specification<TrackingCategory>) (root, query, builder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(builder.equal(root.get("category").get("id"), targetId));
            predicateList.add(builder.equal(root.get("ip"), ip));

            return builder.and(predicateList.toArray(new Predicate[0]));
        };
    }
}
