package com.uit.realestate.service.tracking;

import com.uit.realestate.domain.tracking.TrackingCategory;
import com.uit.realestate.repository.tracking.TrackingCategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TrackingCategoryService {

    private final TrackingCategoryRepository trackingCategoryRepository;

    public TrackingCategoryService(TrackingCategoryRepository trackingCategoryRepository) {
        this.trackingCategoryRepository = trackingCategoryRepository;
    }

    public void tracking(Long userId, String ip, Long targetId, Long rating) {
        if (targetId == null) return;

        log.info("Tracking category " + targetId + "with UserId:" + userId + "And IP" + ip);
        List<TrackingCategory> trackingCategories = trackingCategoryRepository
                .findAll(getTrackingCategorySpecification(userId, ip, targetId));
        if (trackingCategories.isEmpty()) {
            trackingCategoryRepository.save(new TrackingCategory(userId, targetId, ip, rating));
        } else {
            trackingCategories.stream().filter(trackingCategory -> {
                if (userId == null) {
                    return trackingCategory.getUser() == null;
                } else {
                    return trackingCategory.getUser() != null && trackingCategory.getUser().getId().equals(userId);
                }
            }).forEach(trackingCategory -> {
                trackingCategory.setRating(trackingCategory.getRating() + rating);
            });
            trackingCategoryRepository.saveAll(trackingCategories);
        }
    }

    private Specification<TrackingCategory> getTrackingCategorySpecification(Long userId, String ip, Long targetId) {
        return (Specification<TrackingCategory>) (root, query, builder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(builder.equal(root.get("category").get("id"), targetId));
            predicateList.add(builder.equal(root.get("ip"), ip));

            return builder.and(predicateList.toArray(new Predicate[0]));
        };
    }
}
