package com.uit.realestate.repository.location;

import com.uit.realestate.domain.location.District;
import com.uit.realestate.domain.location.Province;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Long> {

    List<District> findAllByProvince_Id(Long provinceId);

    boolean existsById(Long id);
}
