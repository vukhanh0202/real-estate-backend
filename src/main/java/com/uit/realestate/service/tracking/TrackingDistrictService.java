package com.uit.realestate.service.tracking;

import com.uit.realestate.domain.tracking.TrackingDistrict;
import com.uit.realestate.repository.tracking.TrackingDistrictRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TrackingDistrictService{

    @Autowired
    private TrackingDistrictRepository trackingDistrictRepository;

    public void tracking(Long userId, String ip, Long targetId, Long rating) {
        if (targetId == null) return;

        log.info("Tracking country: " + targetId + "with UserId:" + userId + "And IP" + ip);

        List<TrackingDistrict> trackingDistricts = trackingDistrictRepository
                .findAll(getTrackingDistrictSpecification(userId, ip, targetId));
        if (trackingDistricts.isEmpty()) {
            trackingDistrictRepository.save(new TrackingDistrict(userId, targetId, ip, rating));
        } else {
            trackingDistricts.forEach(trackingDistrict -> {
                trackingDistrict.setRating(trackingDistrict.getRating() + rating);
            });
            trackingDistrictRepository.saveAll(trackingDistricts);
        }
    }

    private Specification<TrackingDistrict> getTrackingDistrictSpecification(Long userId, String ip, Long targetId) {
        return (Specification<TrackingDistrict>) (root, query, builder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(builder.equal(root.get("district").get("id"), targetId));
            if (userId != null) {
                predicateList.add(builder.equal(root.get("user").get("id"), userId));
            } else {
                predicateList.add(builder.isEmpty(root.get("user").get("id")));
            }
            if (ip != null) {
                predicateList.add(builder.equal(root.get("ip"), ip));
            } else {
                predicateList.add(builder.isEmpty(root.get("ip")));
            }
            return builder.and(predicateList.toArray(new Predicate[0]));
        };
    }
}
