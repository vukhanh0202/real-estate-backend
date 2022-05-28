package com.uit.realestate.repository.tracking;

import com.uit.realestate.domain.tracking.TrackingCategory;
import com.uit.realestate.domain.tracking.TrackingDirection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TrackingDirectionRepository extends JpaRepository<TrackingDirection, Long>, JpaSpecificationExecutor<TrackingDirection> {

    List<TrackingDirection> findAllByUserIdOrIp(Long userId, String ip);
}
