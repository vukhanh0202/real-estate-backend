package com.uit.realestate.repository.apartment;

import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.domain.apartment.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApartmentRepository extends JpaRepository<Apartment, Long>, JpaSpecificationExecutor<Apartment> {

    List<Apartment> findAllByTitleContainingIgnoreCase(String title);
    List<Apartment> findAllByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String title);
    List<Apartment> findAllByStatusAndPriceBetweenAndAreaBetween(EApartmentStatus status, Double priceFrom, Double priceTo, Double areaFrom, Double areaTo);
    List<Apartment> findAllByStatusAndPriceBetweenAndAreaGreaterThan(EApartmentStatus status, Double priceFrom, Double priceTo, Double areaFrom);
    List<Apartment> findAllByStatusAndPriceGreaterThanAndAreaBetween(EApartmentStatus status, Double priceFrom, Double areaFrom, Double areaTo);
    List<Apartment> findAllByStatusAndPriceGreaterThanAndAreaGreaterThan(EApartmentStatus status,Double priceFrom, Double areaFrom);

    List<Apartment> findAllByStatusAndAreaBetween(EApartmentStatus status, Double areaFrom, Double areaTo);
    List<Apartment> findAllByStatusAndAreaGreaterThan(EApartmentStatus status, Double areaFrom);
    List<Apartment> findAllByStatusAndPriceBetween(EApartmentStatus status, Double priceFrom, Double priceTo);
    List<Apartment> findAllByStatusAndPriceGreaterThan(EApartmentStatus status, Double priceFrom);
    List<Apartment> findAllByApartmentAddressProvinceIdAndStatusAndAreaBetween(Long provinceId, EApartmentStatus status, Double areaFrom, Double areaTo);
    List<Apartment> findAllByApartmentAddressProvinceIdAndStatusAndAreaGreaterThan(Long provinceId, EApartmentStatus status, Double areaFrom);
    List<Apartment> findAllByApartmentAddressProvinceIdAndStatusAndPriceBetween(Long provinceId, EApartmentStatus status, Double priceFrom, Double priceTo);
    List<Apartment> findAllByApartmentAddressProvinceIdAndStatusAndPriceGreaterThan(Long provinceId, EApartmentStatus status, Double priceFrom);

    List<Apartment> findAllByStatusAndTitleContainingIgnoreCase(EApartmentStatus status, String title);

    List<Apartment> findTop4ByStatusOrderByCreatedAtDesc(EApartmentStatus status);

    List<Apartment> findTop4ByHighlightTrueAndStatusOrderByUpdatedAtDesc(EApartmentStatus status);

    List<Apartment> findAllByIdIn(List<Long> ids);

    @Query(value = "SELECT ap.*, (SUM(COALESCE(tc.rating, 0)) + SUM(COALESCE(tp.rating, 0)) + SUM(COALESCE(td.rating, 0))) as rating\n" +
            " FROM apartment ap " +
            " JOIN apartment_address ad ON ap.id = ad.id " +
            " FULL OUTER JOIN tracking_category tc ON tc.category_id = ap.category_id " +
            " FULL OUTER JOIN tracking_province tp ON tp.province_id = ad.province_id " +
            " FULL OUTER JOIN tracking_district td ON td.district_id = ad.district_id " +
            " WHERE ((tc.ip = :ip OR tc.user_id = :userId) " +
            "       OR (tp.ip = :ip OR tp.user_id = :userId) " +
            "       OR (td.ip = :ip OR td.user_id = :userId)) " +
            "       AND ap.status = 'OPEN' " +
            " GROUP BY ap.id " +
            " ORDER BY rating DESC, ap.created_at DESC ",
            nativeQuery = true)
    Page<Apartment> findRecommendApartmentByUserIdAndIp(Long userId, String ip, Pageable pageable);

    Page<Apartment> findAllByAuthorIdAndStatusIn(Long userId, List<EApartmentStatus> status, Pageable pageable);

    @Query(value = "SELECT ap.*, (SUM(COALESCE(tc.rating, 0)) + SUM(COALESCE(tp.rating, 0)) + SUM(COALESCE(td.rating, 0))) as rating\n" +
            " FROM apartment ap " +
            " JOIN apartment_address ad ON ap.id = ad.id " +
            " FULL OUTER JOIN tracking_category tc ON tc.category_id = ap.category_id " +
            " FULL OUTER JOIN tracking_province tp ON tp.province_id = ad.province_id " +
            " FULL OUTER JOIN tracking_district td ON td.district_id = ad.district_id " +
            " WHERE ((tc.user_id = :userId) " +
            "       OR (tp.user_id = :userId) " +
            "       OR (td.user_id = :userId)) " +
            " GROUP BY ap.id " +
            " ORDER BY rating DESC, ap.created_at DESC ",
            nativeQuery = true)
    List<Apartment> findRecommendApartmentByUserId(Long userId);

}
