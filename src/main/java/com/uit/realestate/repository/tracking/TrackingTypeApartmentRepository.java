package com.uit.realestate.repository.tracking;

import com.uit.realestate.domain.tracking.TrackingTypeApartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TrackingTypeApartmentRepository extends JpaRepository<TrackingTypeApartment, Long>, JpaSpecificationExecutor<TrackingTypeApartment> {
}
