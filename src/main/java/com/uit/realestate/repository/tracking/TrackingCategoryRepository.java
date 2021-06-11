package com.uit.realestate.repository.tracking;

import com.uit.realestate.domain.tracking.TrackingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrackingCategoryRepository extends JpaRepository<TrackingCategory, Long>, JpaSpecificationExecutor<TrackingCategory> {

    @Query(value = "SELECT *" +
            " FROM tracking_category t " +
            " WHERE (:userId is null AND :ip = t.ip)" +
            " OR (:userId is not null AND (:userId = t.user_id OR :ip = t.ip))",
            nativeQuery = true)
    List<TrackingCategory> findAllByUserIdAndIp(Long userId, String ip);
}
