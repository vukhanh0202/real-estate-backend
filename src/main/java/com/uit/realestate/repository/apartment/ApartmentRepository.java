package com.uit.realestate.repository.apartment;

import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.domain.apartment.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ApartmentRepository extends JpaRepository<Apartment, Long>, JpaSpecificationExecutor<Apartment> {

    List<Apartment> findTop4ByOrderByCreatedAtDesc();

    List<Apartment> findTop4ByHighlightTrueOrderByUpdatedAtDesc();

}
