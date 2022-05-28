package com.uit.realestate.repository.tracking;

import com.uit.realestate.domain.tracking.TrackingFloor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TrackingFloorRepository extends JpaRepository<TrackingFloor, Long>, JpaSpecificationExecutor<TrackingFloor> {

    List<TrackingFloor> findAllByUserIdOrIp(Long userId, String ip);
}
