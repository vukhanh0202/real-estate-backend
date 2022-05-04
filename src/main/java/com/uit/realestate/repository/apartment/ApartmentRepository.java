package com.uit.realestate.repository.apartment;

import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.domain.apartment.Category;
import com.uit.realestate.payload.apartment.ApartmentQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApartmentRepository extends JpaRepository<Apartment, Long>, JpaSpecificationExecutor<Apartment> {

    List<Apartment> findAllByTitleContainingIgnoreCase(String title);
    List<Apartment> findAllByStatusAndIdIn(EApartmentStatus status, List<Long> ids);
    List<Apartment> findAllByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String title);
    List<Apartment> findAllByStatusAndTypeApartmentAndPriceBetweenAndAreaBetween(EApartmentStatus status, ETypeApartment typeApartment, Double priceFrom, Double priceTo, Double areaFrom, Double areaTo);
    List<Apartment> findAllByStatusAndTypeApartmentAndPriceBetweenAndAreaGreaterThan(EApartmentStatus status, ETypeApartment typeApartment, Double priceFrom, Double priceTo, Double areaFrom);
    List<Apartment> findAllByStatusAndTypeApartmentAndPriceGreaterThanAndAreaBetween(EApartmentStatus status, ETypeApartment typeApartment, Double priceFrom, Double areaFrom, Double areaTo);
    List<Apartment> findAllByStatusAndTypeApartmentAndPriceGreaterThanAndAreaGreaterThan(EApartmentStatus status, ETypeApartment typeApartment,Double priceFrom, Double areaFrom);

    List<Apartment> findAllByStatusAndTypeApartmentAndAreaBetween(EApartmentStatus status, ETypeApartment typeApartment, Double areaFrom, Double areaTo);
    List<Apartment> findAllByStatusAndTypeApartmentAndAreaGreaterThan(EApartmentStatus status, ETypeApartment typeApartment, Double areaFrom);
    List<Apartment> findAllByStatusAndTypeApartmentAndPriceBetween(EApartmentStatus status, ETypeApartment typeApartment, Double priceFrom, Double priceTo);
    List<Apartment> findAllByStatusAndTypeApartmentAndPriceGreaterThan(EApartmentStatus status, ETypeApartment typeApartment, Double priceFrom);
    List<Apartment> findAllByApartmentAddressProvinceIdAndStatusAndTypeApartmentAndAreaBetween(Long provinceId,  EApartmentStatus status,ETypeApartment typeApartment, Double areaFrom, Double areaTo);
    List<Apartment> findAllByApartmentAddressProvinceIdAndStatusAndTypeApartmentAndAreaGreaterThan(Long provinceId,  EApartmentStatus status,ETypeApartment typeApartment, Double areaFrom);
    List<Apartment> findAllByApartmentAddressProvinceIdAndStatusAndTypeApartmentAndPriceBetween(Long provinceId, EApartmentStatus status, ETypeApartment typeApartment, Double priceFrom, Double priceTo);
    List<Apartment> findAllByApartmentAddressProvinceIdAndStatusAndTypeApartmentAndPriceGreaterThan(Long provinceId,  EApartmentStatus status,ETypeApartment typeApartment, Double priceFrom);

    List<Apartment> findAllByStatusAndTitleContainingIgnoreCase(EApartmentStatus status, String title);

    List<Apartment> findTop16ByStatusAndTypeApartmentOrderByCreatedAtDesc(EApartmentStatus status, ETypeApartment typeApartment);

    List<Apartment> findTop4ByHighlightTrueAndStatusAndApartmentAddressProvinceIdOrderByUpdatedAtDesc(EApartmentStatus status, Long provinceId);

    List<Apartment> findAllByIdIn(List<Long> ids);

    @Query(value = "SELECT ap.*, (SUM(COALESCE(tc.rating, 0)) + SUM(COALESCE(tp.rating, 0)) + SUM(COALESCE(td.rating, 0))) as rating\n" +
            " FROM apartment ap " +
            " JOIN apartment_address ad ON ap.id = ad.id " +
            " FULL OUTER JOIN tracking_category tc ON tc.category_id = ap.category_id " +
            " FULL OUTER JOIN tracking_province tp ON tp.province_id = ad.province_id " +
            " FULL OUTER JOIN tracking_district td ON td.district_id = ad.district_id " +
            " WHERE ((tc.ip = :ip OR tc.user_id = :userId) " +
            "           OR (tp.ip = :ip OR tp.user_id = :userId) " +
            "           OR (td.ip = :ip OR td.user_id = :userId)) " +
            "       AND ap.status = 'OPEN' " +
            "       AND ap.type_apartment = :typeApartment " +
            " GROUP BY ap.id " +
            " ORDER BY rating DESC, ap.created_at DESC ",
            nativeQuery = true)
    Page<Apartment> findRecommendApartmentByUserIdAndIp(ETypeApartment typeApartment, Long userId, String ip, Pageable pageable);

    @Query(value = "SELECT ap.*, (SUM(COALESCE(tc.rating, 0)) + SUM(COALESCE(tp.rating, 0)) + SUM(COALESCE(td.rating, 0))) as rating\n" +
            " FROM apartment ap " +
            " JOIN apartment_address ad ON ap.id = ad.id " +
            " JOIN apartment_detail adt ON ap.id = adt.id " +
            " FULL OUTER JOIN tracking_category tc ON tc.category_id = ap.category_id " +
            " FULL OUTER JOIN tracking_province tp ON tp.province_id = ad.province_id " +
            " FULL OUTER JOIN tracking_district td ON td.district_id = ad.district_id " +
            " WHERE ((tc.ip = :ip OR tc.user_id = :userId) " +
            "           OR (tp.ip = :ip OR tp.user_id = :userId) " +
            "           OR (td.ip = :ip OR td.user_id = :userId)) " +
            "       AND ap.status = 'OPEN' " +
            "       AND (COALESCE(:#{#param.bathrooms}) is null OR adt.bathroom_quantity IN :#{#param.bathrooms} ) " +
            "       AND (COALESCE(:#{#param.bedrooms}) is null OR adt.bedroom_quantity IN :#{#param.bedrooms} ) " +
            "       AND (COALESCE(:#{#param.categories}) is null OR ap.category_id IN :#{#param.categories} ) " +
            "       AND (COALESCE(:#{#param.directions}) is null OR adt.house_direction IN :#{#param.directions} ) " +
            "       AND (COALESCE(:#{#param.districts}) is null OR ad.district_id IN :#{#param.districts} )" +
            "       AND (COALESCE(:#{#param.provinces}) is null OR ad.province_id IN :#{#param.provinces} )" +
            "       AND (COALESCE(:#{#param.floors}) is null OR adt.floor_quantity IN :#{#param.floors} )" +
            "       AND (:#{#param.areaLow} is null OR ap.area BETWEEN CAST(CAST(:#{#param.areaLow} AS TEXT) AS DOUBLE PRECISION) AND CAST(CAST(:#{#param.areaHigh} AS TEXT) AS DOUBLE PRECISION)) " +
            "       AND (:#{#param.type} is null OR ap.type_apartment = CAST(:#{#param.type} AS TEXT)) " +
            "       AND (:#{#param.priceLow} is null " +
            "           OR ((:#{#param.type} is null OR :#{#param.type} = 'BUY') AND ap.total_price BETWEEN CAST(CAST(:#{#param.priceLow} AS TEXT) AS DOUBLE PRECISION) AND CAST(CAST(:#{#param.priceHigh} AS TEXT) AS DOUBLE PRECISION)) " +
            "           OR (:#{#param.type} = 'RENT' AND ap.price_rent BETWEEN CAST(CAST(:#{#param.priceLow} AS TEXT) AS DOUBLE PRECISION) AND CAST(CAST(:#{#param.priceHigh} AS TEXT) AS DOUBLE PRECISION))) " +
            " GROUP BY ap.id " +
            " ORDER BY rating DESC, ap.created_at DESC ",
            nativeQuery = true)
    Page<Apartment> findRecommendApartmentForChatBox(@Param("param") ApartmentQueryParam param, Long userId, String ip, Pageable pageable);

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

    @Query(value = "WITH district_tb AS (\n" +
            "   SELECT a.*,  \n" +
            "           SUM(CASE\n" +
            "           WHEN u.district is null THEN 0\n" +
            "           ELSE\n" +
            "           CASE\n" +
            "           WHEN aad.district_id = u.district THEN :accuracy\n" +
            "           ELSE 0\n" +
            "           END\n" +
            "           END) \n" +
            "       AS suitable,\n" +
            "           SUM(CASE\n" +
            "           WHEN u.district is null THEN 0\n" +
            "           ELSE :accuracy\n" +
            "           END)\n" +
            "       AS total\n" +
            "   FROM apartment a, apartment_address aad, user_target u\n" +
            "   WHERE u.user_id = :userId and a.id = aad.id and a.status = 'OPEN'\n" +
            "   GROUP BY a.id \n" +
            "), province_tb AS (\n" +
            "    SELECT a.id, a.title,  \n" +
            "       SUM(CASE\n" +
            "           WHEN u.province is null THEN 0\n" +
            "           ELSE\n" +
            "           CASE\n" +
            "           WHEN aad.province_id = u.province THEN :accuracy\n" +
            "           ELSE 0\n" +
            "           END\n" +
            "           END) \n" +
            "       AS suitable,\n" +
            "       SUM(CASE\n" +
            "           WHEN u.province is null THEN 0\n" +
            "           ELSE :accuracy\n" +
            "           END)\n" +
            "       AS total\n" +
            "   FROM apartment a, apartment_address aad, user_target u\n" +
            "   WHERE u.user_id = :userId and a.id = aad.id and a.status = 'OPEN'\n" +
            "   GROUP BY a.id, a.title \n" +
            "), category_tb AS (\n" +
            "   SELECT a.id, a.title,  \n" +
            "       SUM(CASE\n" +
            "           WHEN u.category is null THEN 0\n" +
            "           ELSE\n" +
            "           CASE\n" +
            "           WHEN a.category_id = u.category THEN :accuracy\n" +
            "           ELSE 0\n" +
            "           END\n" +
            "           END) \n" +
            "       AS suitable,\n" +
            "       SUM(CASE\n" +
            "           WHEN u.category is null THEN 0\n" +
            "           ELSE :accuracy\n" +
            "           END)\n" +
            "       AS total\n" +
            "   FROM apartment a, user_target u\n" +
            "   WHERE u.user_id = :userId and a.status = 'OPEN'\n" +
            "   GROUP BY a.id, a.title \n" +
            "), area_tb AS (\n" +
            "   SELECT a.id, a.title,  \n" +
            "       SUM(CASE\n" +
            "           WHEN u.area is null THEN 0\n" +
            "           ELSE\n" +
            "           CASE\n" +
            "           WHEN ABS(a.area-u.area) >= :accuracyArea THEN 0\n" +
            "           ELSE ABS(:accuracyArea - ABS(a.area-u.area))\n" +
            "           END\n" +
            "           END) \n" +
            "       AS suitable,\n" +
            "       SUM(CASE\n" +
            "           WHEN u.area is null THEN 0\n" +
            "           ELSE :accuracyArea\n" +
            "           END)\n" +
            "       AS total\n" +
            "   FROM apartment a, user_target u\n" +
            "   WHERE u.user_id = :userId and a.status = 'OPEN'\n" +
            "   GROUP BY a.id, a.title \n" +
            "), floor_tb AS (\n" +
            "   SELECT a.id, a.title,  \n" +
            "       SUM(CASE\n" +
            "           WHEN u.floor_quantity is null THEN 0\n" +
            "           ELSE \n" +
            "           CASE\n" +
            "           WHEN ABS(ad.floor_quantity-u.floor_quantity) >= :accuracy THEN 0\n" +
            "           ELSE ABS(:accuracy - ABS(ad.floor_quantity-u.floor_quantity))\n" +
            "           END\n" +
            "           END) \n" +
            "       AS suitable,\n" +
            "       SUM(CASE\n" +
            "           WHEN u.floor_quantity is null THEN 0\n" +
            "           ELSE :accuracy\n" +
            "           END) \n" +
            "       AS total\n" +
            "   FROM apartment a, apartment_detail ad, user_target u\n" +
            "   WHERE u.user_id = :userId and a.id = ad.id and a.status = 'OPEN'\n" +
            "   GROUP BY a.id, a.title \n" +
            "), bedroom_tb AS (\n" +
            "   SELECT a.id, a.title,  \n" +
            "       SUM(CASE\n" +
            "           WHEN u.bedroom_quantity is null THEN 0\n" +
            "           ELSE \n" +
            "           CASE\n" +
            "           WHEN ABS(ad.bedroom_quantity-u.bedroom_quantity) >= :accuracy THEN 0\n" +
            "           ELSE ABS(:accuracy - ABS(ad.bedroom_quantity-u.bedroom_quantity))\n" +
            "           END\n" +
            "           END) \n" +
            "       AS suitable,\n" +
            "       SUM(CASE\n" +
            "           WHEN u.bedroom_quantity is null THEN 0\n" +
            "           ELSE :accuracy\n" +
            "           END) \n" +
            "       AS total\n" +
            "   FROM apartment a, apartment_detail ad, user_target u\n" +
            "   WHERE u.user_id = :userId and a.id = ad.id and a.status = 'OPEN'\n" +
            "   GROUP BY a.id, a.title \n" +
            "), bathroom_tb AS (\n" +
            "   SELECT a.id, a.title,  \n" +
            "       SUM(CASE\n" +
            "           WHEN u.bathroom_quantity is null THEN 0\n" +
            "           ELSE \n" +
            "           CASE\n" +
            "           WHEN ABS(ad.bathroom_quantity-u.bathroom_quantity) >= :accuracy THEN 0\n" +
            "           ELSE ABS(:accuracy - ABS(ad.bathroom_quantity-u.bathroom_quantity))\n" +
            "           END\n" +
            "           END) \n" +
            "       AS suitable,\n" +
            "       SUM(CASE\n" +
            "           WHEN u.bathroom_quantity is null THEN 0\n" +
            "           ELSE :accuracy\n" +
            "           END) \n" +
            "       AS total\n" +
            "   FROM apartment a, apartment_detail ad, user_target u\n" +
            "   WHERE u.user_id = :userId and a.id = ad.id and a.status = 'OPEN'\n" +
            "   GROUP BY a.id, a.title \n" +
            ")\n" +
            "SELECT dtb.area, dtb.id, dtb.author_id, dtb.category_id, dtb.created_at, dtb.created_by, dtb.highlight, dtb.is_deleted, dtb.photos, dtb.price, dtb.price_rent, dtb.status, dtb.title, dtb.total_price, dtb.type_apartment, dtb.unit_rent, dtb.updated_at, dtb.updated_by,\n" +
            "       SUM((dtb.suitable + ptb.suitable + ctb.suitable + atb.suitable + ftb.suitable + dtb.suitable + batb.suitable)/(dtb.total + ptb.total + ctb.total + atb.total + ftb.total + dtb.total + batb.total))*100 AS suitable_rate\n" +
            "FROM district_tb dtb \n" +
            "JOIN province_tb ptb ON dtb.id = ptb.id\n" +
            "JOIN category_tb ctb ON dtb.id = ctb.id\n" +
            "JOIN area_tb atb ON dtb.id = atb.id\n" +
            "JOIN floor_tb ftb ON dtb.id = ftb.id\n" +
            "JOIN bedroom_tb btb ON dtb.id = btb.id\n" +
            "JOIN bathroom_tb batb ON dtb.id = batb.id\n" +
            "GROUP BY dtb.id, dtb.area, dtb.author_id, dtb.category_id, dtb.created_at, dtb.created_by, dtb.highlight, dtb.is_deleted, dtb.photos, dtb.price, dtb.price_rent, dtb.status, dtb.title, dtb.total_price, dtb.type_apartment, dtb.unit_rent, dtb.updated_at, dtb.updated_by\n" +
            "ORDER BY suitable_rate desc LIMIT :size OFFSET :page",
            nativeQuery = true)
    List<Apartment> findApartmentWithSuitableDesc(Long accuracy, Long accuracyArea, Long userId, Long page, Long size);

    @Query(value = "WITH district_tb AS (\n" +
            "   SELECT  a.area, a.id, a.author_id, a.category_id, a.created_at, a.created_by, a.highlight, a.is_deleted," +
            "           a.photos, a.price, a.price_rent, a.status, a.title, a.total_price, a.type_apartment, a.unit_rent," +
            "           a.updated_at, a.updated_by, ad.bathroom_quantity, ad.bedroom_quantity, ad.house_direction," +
            "           aad.district_id, aad.province_id, ad.floor_quantity,\n" +
            "           SUM(CASE\n" +
            "           WHEN u.district is null THEN 0\n" +
            "           ELSE\n" +
            "           CASE\n" +
            "           WHEN aad.district_id = u.district THEN :accuracy\n" +
            "           ELSE 0\n" +
            "           END\n" +
            "           END) \n" +
            "       AS suitable,\n" +
            "           SUM(CASE\n" +
            "           WHEN u.district is null THEN 0\n" +
            "           ELSE :accuracy\n" +
            "           END)\n" +
            "       AS total\n" +
            "   FROM apartment a, apartment_address aad, apartment_detail ad, user_target u\n" +
            "   WHERE u.user_id = :userId and a.id = aad.id and a.id = ad.id and a.status = 'OPEN'\n" +
            "   GROUP BY a.id, aad.id, ad.id \n" +
            "), province_tb AS (\n" +
            "    SELECT a.id, a.title,  \n" +
            "       SUM(CASE\n" +
            "           WHEN u.province is null THEN 0\n" +
            "           ELSE\n" +
            "           CASE\n" +
            "           WHEN aad.province_id = u.province THEN :accuracy\n" +
            "           ELSE 0\n" +
            "           END\n" +
            "           END) \n" +
            "       AS suitable,\n" +
            "       SUM(CASE\n" +
            "           WHEN u.province is null THEN 0\n" +
            "           ELSE :accuracy\n" +
            "           END)\n" +
            "       AS total\n" +
            "   FROM apartment a, apartment_address aad, user_target u\n" +
            "   WHERE u.user_id = :userId and a.id = aad.id and a.status = 'OPEN'\n" +
            "   GROUP BY a.id, a.title \n" +
            "), category_tb AS (\n" +
            "   SELECT a.id, a.title,  \n" +
            "       SUM(CASE\n" +
            "           WHEN u.category is null THEN 0\n" +
            "           ELSE\n" +
            "           CASE\n" +
            "           WHEN a.category_id = u.category THEN :accuracy\n" +
            "           ELSE 0\n" +
            "           END\n" +
            "           END) \n" +
            "       AS suitable,\n" +
            "       SUM(CASE\n" +
            "           WHEN u.category is null THEN 0\n" +
            "           ELSE :accuracy\n" +
            "           END)\n" +
            "       AS total\n" +
            "   FROM apartment a, user_target u\n" +
            "   WHERE u.user_id = :userId and a.status = 'OPEN'\n" +
            "   GROUP BY a.id, a.title \n" +
            "), area_tb AS (\n" +
            "   SELECT a.id, a.title,  \n" +
            "       SUM(CASE\n" +
            "           WHEN u.area is null THEN 0\n" +
            "           ELSE\n" +
            "           CASE\n" +
            "           WHEN ABS(a.area-u.area) >= :accuracyArea THEN 0\n" +
            "           ELSE ABS(:accuracyArea - ABS(a.area-u.area))\n" +
            "           END\n" +
            "           END) \n" +
            "       AS suitable,\n" +
            "       SUM(CASE\n" +
            "           WHEN u.area is null THEN 0\n" +
            "           ELSE :accuracyArea\n" +
            "           END)\n" +
            "       AS total\n" +
            "   FROM apartment a, user_target u\n" +
            "   WHERE u.user_id = :userId and a.status = 'OPEN'\n" +
            "   GROUP BY a.id, a.title \n" +
            "), floor_tb AS (\n" +
            "   SELECT a.id, a.title,  \n" +
            "       SUM(CASE\n" +
            "           WHEN u.floor_quantity is null THEN 0\n" +
            "           ELSE \n" +
            "           CASE\n" +
            "           WHEN ABS(ad.floor_quantity-u.floor_quantity) >= :accuracy THEN 0\n" +
            "           ELSE ABS(:accuracy - ABS(ad.floor_quantity-u.floor_quantity))\n" +
            "           END\n" +
            "           END) \n" +
            "       AS suitable,\n" +
            "       SUM(CASE\n" +
            "           WHEN u.floor_quantity is null THEN 0\n" +
            "           ELSE :accuracy\n" +
            "           END) \n" +
            "       AS total\n" +
            "   FROM apartment a, apartment_detail ad, user_target u\n" +
            "   WHERE u.user_id = :userId and a.id = ad.id and a.status = 'OPEN'\n" +
            "   GROUP BY a.id, a.title \n" +
            "), bedroom_tb AS (\n" +
            "   SELECT a.id, a.title,  \n" +
            "       SUM(CASE\n" +
            "           WHEN u.bedroom_quantity is null THEN 0\n" +
            "           ELSE \n" +
            "           CASE\n" +
            "           WHEN ABS(ad.bedroom_quantity-u.bedroom_quantity) >= :accuracy THEN 0\n" +
            "           ELSE ABS(:accuracy - ABS(ad.bedroom_quantity-u.bedroom_quantity))\n" +
            "           END\n" +
            "           END) \n" +
            "       AS suitable,\n" +
            "       SUM(CASE\n" +
            "           WHEN u.bedroom_quantity is null THEN 0\n" +
            "           ELSE :accuracy\n" +
            "           END) \n" +
            "       AS total\n" +
            "   FROM apartment a, apartment_detail ad, user_target u\n" +
            "   WHERE u.user_id = :userId and a.id = ad.id and a.status = 'OPEN'\n" +
            "   GROUP BY a.id, a.title \n" +
            "), bathroom_tb AS (\n" +
            "   SELECT a.id, a.title,  \n" +
            "       SUM(CASE\n" +
            "           WHEN u.bathroom_quantity is null THEN 0\n" +
            "           ELSE \n" +
            "           CASE\n" +
            "           WHEN ABS(ad.bathroom_quantity-u.bathroom_quantity) >= :accuracy THEN 0\n" +
            "           ELSE ABS(:accuracy - ABS(ad.bathroom_quantity-u.bathroom_quantity))\n" +
            "           END\n" +
            "           END) \n" +
            "       AS suitable,\n" +
            "       SUM(CASE\n" +
            "           WHEN u.bathroom_quantity is null THEN 0\n" +
            "           ELSE :accuracy\n" +
            "           END) \n" +
            "       AS total\n" +
            "   FROM apartment a, apartment_detail ad, user_target u\n" +
            "   WHERE u.user_id = :userId and a.id = ad.id and a.status = 'OPEN'\n" +
            "   GROUP BY a.id, a.title \n" +
            ")\n" +
            "SELECT dtb.area, dtb.id, dtb.author_id, dtb.category_id, dtb.created_at, dtb.created_by, dtb.highlight, dtb.is_deleted, dtb.photos, dtb.price, dtb.price_rent, dtb.status, dtb.title, dtb.total_price, dtb.type_apartment, dtb.unit_rent, dtb.updated_at, dtb.updated_by,\n" +
            "       SUM((dtb.suitable + ptb.suitable + ctb.suitable + atb.suitable + ftb.suitable + dtb.suitable + batb.suitable)/(dtb.total + ptb.total + ctb.total + atb.total + ftb.total + dtb.total + batb.total))*100 AS suitable_rate\n" +
            "FROM district_tb dtb \n" +
            "JOIN province_tb ptb ON dtb.id = ptb.id\n" +
            "JOIN category_tb ctb ON dtb.id = ctb.id\n" +
            "JOIN area_tb atb ON dtb.id = atb.id\n" +
            "JOIN floor_tb ftb ON dtb.id = ftb.id\n" +
            "JOIN bedroom_tb btb ON dtb.id = btb.id\n" +
            "JOIN bathroom_tb batb ON dtb.id = batb.id\n" +
            "WHERE  (COALESCE(:#{#param.bathrooms}) is null OR dtb.bathroom_quantity IN :#{#param.bathrooms} ) " +
            "       AND (COALESCE(:#{#param.bedrooms}) is null OR dtb.bedroom_quantity IN :#{#param.bedrooms} ) " +
            "       AND (COALESCE(:#{#param.categories}) is null OR dtb.category_id IN :#{#param.categories} ) " +
            "       AND (COALESCE(:#{#param.directions}) is null OR dtb.house_direction IN :#{#param.directions} ) " +
            "       AND (COALESCE(:#{#param.districts}) is null OR dtb.district_id IN :#{#param.districts} )" +
            "       AND (COALESCE(:#{#param.provinces}) is null OR dtb.province_id IN :#{#param.provinces} )" +
            "       AND (COALESCE(:#{#param.floors}) is null OR dtb.floor_quantity IN :#{#param.floors} )" +
            "       AND (:#{#param.areaLow} is null OR dtb.area BETWEEN CAST(CAST(:#{#param.areaLow} AS TEXT) AS DOUBLE PRECISION) AND CAST(CAST(:#{#param.areaHigh} AS TEXT) AS DOUBLE PRECISION)) " +
            "       AND (:#{#param.type} is null OR dtb.type_apartment = CAST(:#{#param.type} AS TEXT)) " +
            "       AND (:#{#param.priceLow} is null " +
            "           OR ((:#{#param.type} is null OR :#{#param.type} = 'BUY') AND dtb.total_price BETWEEN CAST(CAST(:#{#param.priceLow} AS TEXT) AS DOUBLE PRECISION) AND CAST(CAST(:#{#param.priceHigh} AS TEXT) AS DOUBLE PRECISION)) " +
            "           OR (:#{#param.type} = 'RENT' AND dtb.price_rent BETWEEN CAST(CAST(:#{#param.priceLow} AS TEXT) AS DOUBLE PRECISION) AND CAST(CAST(:#{#param.priceHigh} AS TEXT) AS DOUBLE PRECISION))) " +
            "GROUP BY dtb.id, dtb.area, dtb.author_id, dtb.category_id, dtb.created_at, dtb.created_by, dtb.highlight, dtb.is_deleted, dtb.photos, dtb.price, dtb.price_rent, dtb.status, dtb.title, dtb.total_price, dtb.type_apartment, dtb.unit_rent, dtb.updated_at, dtb.updated_by\n" +
            "ORDER BY suitable_rate desc LIMIT :size OFFSET :page",
            nativeQuery = true)
    List<Apartment> findSuitableApartmentForChatBox(@Param("param") ApartmentQueryParam param, Long accuracy, Long accuracyArea, Long userId, Integer page, Integer size);
}
