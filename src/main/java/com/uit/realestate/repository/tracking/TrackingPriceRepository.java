package com.uit.realestate.repository.tracking;

import com.uit.realestate.domain.tracking.TrackingPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TrackingPriceRepository extends JpaRepository<TrackingPrice, Long>, JpaSpecificationExecutor<TrackingPrice> {

    List<TrackingPrice> findAllByUserIdOrIp(Long userId, String ip);
}
