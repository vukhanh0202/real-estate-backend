package com.uit.realestate.repository.tracking;

import com.uit.realestate.domain.tracking.TrackingArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TrackingAreaRepository extends JpaRepository<TrackingArea, Long>, JpaSpecificationExecutor<TrackingArea> {

    List<TrackingArea> findAllByUserIdOrIp(Long userId, String ip);
}
