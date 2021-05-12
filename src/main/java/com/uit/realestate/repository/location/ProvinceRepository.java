package com.uit.realestate.repository.location;

import com.uit.realestate.domain.location.Country;
import com.uit.realestate.domain.location.Province;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProvinceRepository extends JpaRepository<Province, Long> {

    List<Province> findAllByCountry_Code(String countryCode);
}
