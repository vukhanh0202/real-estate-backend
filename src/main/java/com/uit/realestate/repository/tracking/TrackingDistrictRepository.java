package com.uit.realestate.repository.tracking;

import com.uit.realestate.domain.tracking.TrackingDistrict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrackingDistrictRepository extends JpaRepository<TrackingDistrict, Long>, JpaSpecificationExecutor<TrackingDistrict> {

    @Query(value = "SELECT *" +
            " FROM tracking_district t " +
            " WHERE (:userId is null AND :ip = t.ip)" +
            " OR (:userId is not null AND (:userId = t.user_id OR :ip = t.ip))",
            nativeQuery = true)
    List<TrackingDistrict> findAllByUserIdAndIp(Long userId, String ip);
}
