package com.uit.realestate.service.tracking;

import com.uit.realestate.constant.enums.ETrackingType;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.domain.tracking.TrackingCategory;
import com.uit.realestate.domain.tracking.TrackingDistrict;
import com.uit.realestate.domain.tracking.TrackingProvince;
import com.uit.realestate.domain.tracking.TrackingTypeApartment;
import com.uit.realestate.repository.category.CategoryRepository;
import com.uit.realestate.repository.location.DistrictRepository;
import com.uit.realestate.repository.location.ProvinceRepository;
import com.uit.realestate.repository.tracking.TrackingCategoryRepository;
import com.uit.realestate.repository.tracking.TrackingDistrictRepository;
import com.uit.realestate.repository.tracking.TrackingProvinceRepository;
import com.uit.realestate.repository.tracking.TrackingTypeApartmentRepository;
import com.uit.realestate.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TrackingService {

    private final TrackingCategoryRepository trackingCategoryRepository;
    private final TrackingProvinceRepository trackingProvinceRepository;
    private final TrackingDistrictRepository trackingDistrictRepository;
    private final TrackingTypeApartmentRepository trackingTypeApartmentRepository;

    private final CategoryRepository categoryRepository;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final UserRepository userRepository;

    public void tracking(Long userId, String ip, Map<ETrackingType, String> mapTarget, Long rating) {
        if (Objects.isNull(userId) || userRepository.findById(userId).isEmpty()){
            return;
        }
        try {
            for (ETrackingType trackingType : mapTarget.keySet()) {
                switch (trackingType) {
                    case CATEGORY:
                        trackingCategory(userId, ip, Long.valueOf(mapTarget.get(trackingType)), rating);
                        break;
                    case DISTRICT:
                        trackingDistrict(userId, ip, Long.valueOf(mapTarget.get(trackingType)), rating);
                        break;
                    case PROVINCE:
                        trackingProvince(userId, ip, Long.valueOf(mapTarget.get(trackingType)), rating);
                        break;
                    case TYPE:
                        trackingTypeApartment(userId, ip, mapTarget.get(trackingType), rating);
                        break;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void trackingCategory(Long userId, String ip, Long targetId, Long rating) {
        if (targetId == null) return;

        log.info("Tracking category: " + targetId + "with UserId:" + userId + "And IP" + ip);
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

    public void trackingTypeApartment(Long userId, String ip, String targetId, Long rating) {
        if (targetId == null) return;

        log.info("Tracking Type Apartment: " + targetId + "with UserId:" + userId + "And IP" + ip);

        List<TrackingTypeApartment> trackingTypeApartments = trackingTypeApartmentRepository
                .findAll(getTrackingTypeApartmentSpecification(ip, ETypeApartment.valueOf(targetId)));
        trackingTypeApartments = trackingTypeApartments.stream().filter(trackingTypeApartment -> {
            if (userId == null) {
                return trackingTypeApartment.getUser() == null;
            } else {
                return trackingTypeApartment.getUser() != null && trackingTypeApartment.getUser().getId().equals(userId);
            }
        }).collect(Collectors.toList());
        if (trackingTypeApartments.isEmpty()) {
            trackingTypeApartmentRepository.save(new TrackingTypeApartment(userId, ETypeApartment.valueOf(targetId), ip, rating));
        } else {
            trackingTypeApartments.forEach(trackingTypeApartment -> {
                trackingTypeApartment.setRating(trackingTypeApartment.getRating() + rating);
            });
            trackingTypeApartmentRepository.saveAll(trackingTypeApartments);
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

    private Specification<TrackingTypeApartment> getTrackingTypeApartmentSpecification(String ip, ETypeApartment targetId) {
        return (Specification<TrackingTypeApartment>) (root, query, builder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(builder.equal(root.get("typeApartment"), targetId));
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
