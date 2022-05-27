package com.uit.realestate.repository.tracking;

import com.uit.realestate.domain.tracking.TrackingDistrict;
import com.uit.realestate.domain.tracking.TrackingProvince;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TrackingProvinceRepository extends JpaRepository<TrackingProvince, Long>, JpaSpecificationExecutor<TrackingProvince> {
    List<TrackingProvince> findAllByUserIdOrIp(Long userId, String ip);

}
