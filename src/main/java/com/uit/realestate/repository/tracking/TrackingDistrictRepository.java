package com.uit.realestate.repository.tracking;

import com.uit.realestate.domain.tracking.TrackingCategory;
import com.uit.realestate.domain.tracking.TrackingDistrict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TrackingDistrictRepository extends JpaRepository<TrackingDistrict, Long>, JpaSpecificationExecutor<TrackingDistrict> {
    List<TrackingDistrict> findAllByUserIdOrIp(Long userId, String ip);
}
