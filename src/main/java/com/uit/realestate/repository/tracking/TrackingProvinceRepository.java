package com.uit.realestate.repository.tracking;

import com.uit.realestate.domain.tracking.TrackingCategory;
import com.uit.realestate.domain.tracking.TrackingProvince;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TrackingProvinceRepository extends JpaRepository<TrackingProvince, Long>, JpaSpecificationExecutor<TrackingProvince> {
}
