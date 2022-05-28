package com.uit.realestate.repository.tracking;

import com.uit.realestate.domain.tracking.TrackingToilet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TrackingToiletRepository extends JpaRepository<TrackingToilet, Long>, JpaSpecificationExecutor<TrackingToilet> {

    List<TrackingToilet> findAllByUserIdOrIp(Long userId, String ip);
}
