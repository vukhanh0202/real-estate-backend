package com.uit.realestate.repository.tracking;

import com.uit.realestate.domain.tracking.TrackingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TrackingCategoryRepository extends JpaRepository<TrackingCategory, Long>, JpaSpecificationExecutor<TrackingCategory> {

    List<TrackingCategory> findAllByUserIdOrIp(Long userId, String ip);
}
