package com.uit.realestate.repository.location;

import com.uit.realestate.domain.location.Country;
import com.uit.realestate.domain.location.Province;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProvinceRepository extends JpaRepository<Province, Long> {

    List<Province> findAllByCountry_Code(String countryCode);

    boolean existsById(Long id);

    @Query(value = "select p from Province p join p.apartmentAddresses ad group by p Order By count(ad) desc",
            countQuery = "select count(p) from Province p")
    List<Province> findAllByApartmentAddressesCountDesc(Pageable pageable);


    @Query(value = "select p from Province p join p.apartmentAddresses ad group by p",
            countQuery = "select count(p) from Province p")
    List<Province> findAllByApartmentPrices();

}
