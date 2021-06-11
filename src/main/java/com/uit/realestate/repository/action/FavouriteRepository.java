package com.uit.realestate.repository.action;

import com.uit.realestate.domain.action.Favourite;
import com.uit.realestate.domain.action.id.ApartmentUserId;
import com.uit.realestate.domain.apartment.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavouriteRepository extends JpaRepository<Favourite, ApartmentUserId> {

    Optional<Favourite> findByApartmentIdAndUserId(Long apartmentId, Long userId);
}
