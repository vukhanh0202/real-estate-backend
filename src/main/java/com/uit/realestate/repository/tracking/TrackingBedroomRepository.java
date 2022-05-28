package com.uit.realestate.repository.tracking;

import com.uit.realestate.domain.tracking.TrackingBedroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TrackingBedroomRepository extends JpaRepository<TrackingBedroom, Long>, JpaSpecificationExecutor<TrackingBedroom> {

    List<TrackingBedroom> findAllByUserIdOrIp(Long userId, String ip);
}
