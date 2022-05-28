package com.uit.realestate.service.tracking;

import com.uit.realestate.constant.enums.ETrackingType;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.domain.tracking.*;
import com.uit.realestate.repository.category.CategoryRepository;
import com.uit.realestate.repository.location.DistrictRepository;
import com.uit.realestate.repository.location.ProvinceRepository;
import com.uit.realestate.repository.tracking.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
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
    private final TrackingTypeApartmentRepository trackingTypeApartmentRepository;
    private final TrackingAreaRepository trackingAreaRepository;
    private final TrackingBathRoomRepository trackingBathRoomRepository;
    private final TrackingBedroomRepository trackingBedroomRepository;
    private final TrackingDirectionRepository trackingDirectionRepository;
    private final TrackingFloorRepository trackingFloorRepository;
    private final TrackingPriceRepository trackingPriceRepository;
    private final TrackingToiletRepository trackingToiletRepository;

    private final CategoryRepository categoryRepository;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;

    private boolean isEmptyUserId(Long userId){
        return userId == null || userId == -1;
    }

    @Async
    public void tracking(Long userId, String ip, Map<ETrackingType, String> mapTarget, Long rating) {
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
                    case AREA:
                        trackingArea(userId, ip, Double.valueOf(mapTarget.get(trackingType)), rating);
                        break;
                    case BATHROOM:
                        trackingBathroom(userId, ip, Integer.valueOf(mapTarget.get(trackingType)), rating);
                        break;
                    case BEDROOM:
                        trackingBedroom(userId, ip, Integer.valueOf(mapTarget.get(trackingType)), rating);
                        break;
                    case DIRECTION:
                        trackingDirection(userId, ip, mapTarget.get(trackingType), rating);
                        break;
                    case FLOOR:
                        trackingFloor(userId, ip, Integer.valueOf(mapTarget.get(trackingType)), rating);
                        break;
                    case PRICE:
                        trackingPrice(userId, ip, Double.valueOf(mapTarget.get(trackingType)), rating);
                        break;
                    case TOILET:
                        trackingToilet(userId, ip, Integer.valueOf(mapTarget.get(trackingType)), rating);
                        break;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void trackingToilet(Long userId, String ip, Integer targetId, Long rating) {
        if (targetId == null) return;

        log.info("Tracking Toilet: " + targetId + "with UserId:" + userId + "And IP" + ip);
        List<TrackingToilet> trackingToilets = trackingToiletRepository
                .findAll(getTrackingToiletSpecification(ip, targetId));
        trackingToilets = trackingToilets.stream().filter(trackingToilet -> {
            if (isEmptyUserId(userId)) {
                return trackingToilet.getUser() == null;
            } else {
                return trackingToilet.getUser() != null && trackingToilet.getUser().getId().equals(userId);
            }
        }).collect(Collectors.toList());
        if (trackingToilets.isEmpty()) {
            trackingToiletRepository.save(new TrackingToilet(userId, targetId, ip, rating));
        } else {
            trackingToilets.forEach(trackingToilet -> {
                trackingToilet.setRating(trackingToilet.getRating() + rating);
            });
            trackingToiletRepository.saveAll(trackingToilets);
        }
    }

    private void trackingPrice(Long userId, String ip, Double targetId, Long rating) {
        if (targetId == null) return;

        log.info("Tracking Price: " + targetId + "with UserId:" + userId + "And IP" + ip);
        List<TrackingPrice> trackingPrices = trackingPriceRepository
                .findAll(getTrackingPriceSpecification(ip, targetId));
        trackingPrices = trackingPrices.stream().filter(trackingPrice -> {
            if (isEmptyUserId(userId)) {
                return trackingPrice.getUser() == null;
            } else {
                return trackingPrice.getUser() != null && trackingPrice.getUser().getId().equals(userId);
            }
        }).collect(Collectors.toList());
        if (trackingPrices.isEmpty()) {
            trackingPriceRepository.save(new TrackingPrice(userId, targetId, ip, rating));
        } else {
            trackingPrices.forEach(trackingPrice -> {
                trackingPrice.setRating(trackingPrice.getRating() + rating);
            });
            trackingPriceRepository.saveAll(trackingPrices);
        }
    }

    private void trackingFloor(Long userId, String ip, Integer targetId, Long rating) {
        if (targetId == null) return;

        log.info("Tracking Floor: " + targetId + "with UserId:" + userId + "And IP" + ip);
        List<TrackingFloor> trackingFloors = trackingFloorRepository
                .findAll(getTrackingFloorSpecification(ip, targetId));
        trackingFloors = trackingFloors.stream().filter(trackingFloor -> {
            if (isEmptyUserId(userId)) {
                return trackingFloor.getUser() == null;
            } else {
                return trackingFloor.getUser() != null && trackingFloor.getUser().getId().equals(userId);
            }
        }).collect(Collectors.toList());
        if (trackingFloors.isEmpty()) {
            trackingFloorRepository.save(new TrackingFloor(userId, targetId, ip, rating));
        } else {
            trackingFloors.forEach(trackingFloor -> {
                trackingFloor.setRating(trackingFloor.getRating() + rating);
            });
            trackingFloorRepository.saveAll(trackingFloors);
        }
    }

    private void trackingDirection(Long userId, String ip, String targetId, Long rating) {
        if (targetId == null) return;

        log.info("Tracking Direction: " + targetId + "with UserId:" + userId + "And IP" + ip);
        List<TrackingDirection> trackingDirections = trackingDirectionRepository
                .findAll(getTrackingDirectionSpecification(ip, targetId));
        trackingDirections = trackingDirections.stream().filter(trackingDirection -> {
            if (isEmptyUserId(userId)) {
                return trackingDirection.getUser() == null;
            } else {
                return trackingDirection.getUser() != null && trackingDirection.getUser().getId().equals(userId);
            }
        }).collect(Collectors.toList());
        if (trackingDirections.isEmpty()) {
            trackingDirectionRepository.save(new TrackingDirection(userId, targetId, ip, rating));
        } else {
            trackingDirections.forEach(trackingDirection -> {
                trackingDirection.setRating(trackingDirection.getRating() + rating);
            });
            trackingDirectionRepository.saveAll(trackingDirections);
        }
    }

    private void trackingBedroom(Long userId, String ip, Integer targetId, Long rating) {
        if (targetId == null) return;

        log.info("Tracking Bedroom: " + targetId + "with UserId:" + userId + "And IP" + ip);
        List<TrackingBedroom> trackingBedrooms = trackingBedroomRepository
                .findAll(getTrackingBedroomSpecification(ip, targetId));
        trackingBedrooms = trackingBedrooms.stream().filter(trackingBedroom -> {
            if (isEmptyUserId(userId)) {
                return trackingBedroom.getUser() == null;
            } else {
                return trackingBedroom.getUser() != null && trackingBedroom.getUser().getId().equals(userId);
            }
        }).collect(Collectors.toList());
        if (trackingBedrooms.isEmpty()) {
            trackingBedroomRepository.save(new TrackingBedroom(userId, targetId, ip, rating));
        } else {
            trackingBedrooms.forEach(trackingBedroom -> {
                trackingBedroom.setRating(trackingBedroom.getRating() + rating);
            });
            trackingBedroomRepository.saveAll(trackingBedrooms);
        }
    }

    private void trackingBathroom(Long userId, String ip, Integer targetId, Long rating) {
        if (targetId == null) return;

        log.info("Tracking Bathroom: " + targetId + "with UserId:" + userId + "And IP" + ip);
        List<TrackingBathroom> trackingBathrooms = trackingBathRoomRepository
                .findAll(getTrackingBathroomSpecification(ip, targetId));
        trackingBathrooms = trackingBathrooms.stream().filter(trackingBathroom -> {
            if (isEmptyUserId(userId)) {
                return trackingBathroom.getUser() == null;
            } else {
                return trackingBathroom.getUser() != null && trackingBathroom.getUser().getId().equals(userId);
            }
        }).collect(Collectors.toList());
        if (trackingBathrooms.isEmpty()) {
            trackingBathRoomRepository.save(new TrackingBathroom(userId, targetId, ip, rating));
        } else {
            trackingBathrooms.forEach(trackingBathroom -> {
                trackingBathroom.setRating(trackingBathroom.getRating() + rating);
            });
            trackingBathRoomRepository.saveAll(trackingBathrooms);
        }
    }

    private void trackingArea(Long userId, String ip, Double targetId, Long rating) {
        if (targetId == null) return;

        log.info("Tracking Area: " + targetId + "with UserId:" + userId + "And IP" + ip);
        List<TrackingArea> trackingAreas = trackingAreaRepository
                .findAll(getTrackingAreaSpecification(ip, targetId));
        trackingAreas = trackingAreas.stream().filter(trackingArea -> {
            if (isEmptyUserId(userId)) {
                return trackingArea.getUser() == null;
            } else {
                return trackingArea.getUser() != null && trackingArea.getUser().getId().equals(userId);
            }
        }).collect(Collectors.toList());
        if (trackingAreas.isEmpty()) {
            trackingAreaRepository.save(new TrackingArea(userId, targetId, ip, rating));
        } else {
            trackingAreas.forEach(trackingArea -> {
                trackingArea.setRating(trackingArea.getRating() + rating);
            });
            trackingAreaRepository.saveAll(trackingAreas);
        }
    }

    public void trackingCategory(Long userId, String ip, Long targetId, Long rating) {
        if (targetId == null) return;

        log.info("Tracking category: " + targetId + "with UserId:" + userId + "And IP" + ip);
        List<TrackingCategory> trackingCategories = trackingCategoryRepository
                .findAll(getTrackingCategorySpecification(ip, targetId));
        trackingCategories = trackingCategories.stream().filter(trackingCategory -> {
            if (isEmptyUserId(userId)) {
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
            if (isEmptyUserId(userId)) {
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
            if (isEmptyUserId(userId)) {
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
            if (isEmptyUserId(userId)) {
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

    private Specification<TrackingArea> getTrackingAreaSpecification(String ip, Double targetId) {
        return (Specification<TrackingArea>) (root, query, builder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(builder.equal(root.get("area"), targetId));
            predicateList.add(builder.equal(root.get("ip"), ip));

            return builder.and(predicateList.toArray(new Predicate[0]));
        };
    }

    private Specification<TrackingPrice> getTrackingPriceSpecification(String ip, Double targetId) {
        return (Specification<TrackingPrice>) (root, query, builder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(builder.equal(root.get("price"), targetId));
            predicateList.add(builder.equal(root.get("ip"), ip));

            return builder.and(predicateList.toArray(new Predicate[0]));
        };
    }

    private Specification<TrackingBathroom> getTrackingBathroomSpecification(String ip, Integer targetId) {
        return (Specification<TrackingBathroom>) (root, query, builder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(builder.equal(root.get("bathroom"), targetId));
            predicateList.add(builder.equal(root.get("ip"), ip));

            return builder.and(predicateList.toArray(new Predicate[0]));
        };
    }

    private Specification<TrackingBedroom> getTrackingBedroomSpecification(String ip, Integer targetId) {
        return (Specification<TrackingBedroom>) (root, query, builder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(builder.equal(root.get("bedroom"), targetId));
            predicateList.add(builder.equal(root.get("ip"), ip));

            return builder.and(predicateList.toArray(new Predicate[0]));
        };
    }

    private Specification<TrackingFloor> getTrackingFloorSpecification(String ip, Integer targetId) {
        return (Specification<TrackingFloor>) (root, query, builder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(builder.equal(root.get("floor"), targetId));
            predicateList.add(builder.equal(root.get("ip"), ip));

            return builder.and(predicateList.toArray(new Predicate[0]));
        };
    }

    private Specification<TrackingDirection> getTrackingDirectionSpecification(String ip, String targetId) {
        return (Specification<TrackingDirection>) (root, query, builder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(builder.equal(root.get("direction"), targetId));
            predicateList.add(builder.equal(root.get("ip"), ip));

            return builder.and(predicateList.toArray(new Predicate[0]));
        };
    }

    private Specification<TrackingToilet> getTrackingToiletSpecification(String ip, Integer targetId) {
        return (Specification<TrackingToilet>) (root, query, builder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(builder.equal(root.get("toilet"), targetId));
            predicateList.add(builder.equal(root.get("ip"), ip));

            return builder.and(predicateList.toArray(new Predicate[0]));
        };
    }
}
