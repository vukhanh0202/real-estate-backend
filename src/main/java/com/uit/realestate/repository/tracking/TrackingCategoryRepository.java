package com.uit.realestate.repository.tracking;

import com.uit.realestate.domain.tracking.TrackingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TrackingCategoryRepository extends JpaRepository<TrackingCategory, Long>, JpaSpecificationExecutor<TrackingCategory> {
}
