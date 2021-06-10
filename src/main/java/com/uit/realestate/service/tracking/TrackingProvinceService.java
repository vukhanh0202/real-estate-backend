package com.uit.realestate.service.tracking;

import com.uit.realestate.domain.tracking.TrackingProvince;
import com.uit.realestate.repository.tracking.TrackingProvinceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TrackingProvinceService{

    @Autowired
    private TrackingProvinceRepository trackingProvinceRepository;

    public void tracking(Long userId, String ip, Long targetId, Long rating) {
        if (targetId == null) return;

        log.info("Tracking province: " + targetId + "with UserId:" + userId + "And IP" + ip);

        List<TrackingProvince> trackingProvinces = trackingProvinceRepository
                .findAll(getTrackingProvinceSpecification(userId, ip, targetId));
        if (trackingProvinces.isEmpty()) {
            trackingProvinceRepository.save(new TrackingProvince(userId, targetId, ip, rating));
        } else {
            trackingProvinces.forEach(trackingProvince -> {
                trackingProvince.setRating(trackingProvince.getRating() + rating);
            });
            trackingProvinceRepository.saveAll(trackingProvinces);
        }
    }

    private Specification<TrackingProvince> getTrackingProvinceSpecification(Long userId, String ip, Long targetId) {
        return (Specification<TrackingProvince>) (root, query, builder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(builder.equal(root.get("province").get("id"), targetId));
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
