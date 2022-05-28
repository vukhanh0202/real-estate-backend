package com.uit.realestate.repository.tracking;

import com.uit.realestate.domain.tracking.TrackingBathroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TrackingBathRoomRepository extends JpaRepository<TrackingBathroom, Long>, JpaSpecificationExecutor<TrackingBathroom> {

    List<TrackingBathroom> findAllByUserIdOrIp(Long userId, String ip);
}
