package com.uit.realestate.repository.action;

import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.domain.action.Favourite;
import com.uit.realestate.domain.action.id.ApartmentUserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavouriteRepository extends JpaRepository<Favourite, ApartmentUserId> {

    Optional<Favourite> findByApartmentIdAndUserId(Long apartmentId, Long userId);

    Page<Favourite> findAllByUserIdAndApartment_Status(Long userId, EApartmentStatus status, Pageable pageable);
}
