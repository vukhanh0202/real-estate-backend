package com.uit.realestate.repository.location;

import com.uit.realestate.domain.location.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, String> {
}
