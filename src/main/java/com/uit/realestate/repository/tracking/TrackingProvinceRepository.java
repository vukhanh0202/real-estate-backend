package com.uit.realestate.repository.tracking;

import com.uit.realestate.domain.tracking.TrackingProvince;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrackingProvinceRepository extends JpaRepository<TrackingProvince, Long>, JpaSpecificationExecutor<TrackingProvince> {

    @Query(value = "SELECT *" +
            " FROM tracking_province t " +
            " WHERE (:userId is null AND :ip = t.ip)" +
            " OR (:userId is not null AND (:userId = t.user_id OR :ip = t.ip))",
            nativeQuery = true)
    List<TrackingProvince> findAllByUserIdAndIp(Long userId, String ip);
}
