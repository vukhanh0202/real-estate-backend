package com.uit.realestate.repository.apartment;

import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.domain.apartment.ApartmentRating;
import com.uit.realestate.payload.apartment.ApartmentQueryParam;
import com.uit.realestate.payload.apartment.SearchApartmentRequest;
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

    @Query(value = "SELECT * FROM apartment " +
            " WHERE status = :status" +
            " AND type_apartment = :type" +
            " AND (:areaFrom is null OR CAST(CAST(:areaFrom AS TEXT) AS DOUBLE PRECISION) <= area)" +
            " AND (:areaTo is null OR CAST(CAST(:areaTo AS TEXT) AS DOUBLE PRECISION) >= area)" +
            " AND (:priceFrom is null OR (:type = 'BUY' AND CAST(CAST(:priceFrom AS TEXT) AS DOUBLE PRECISION) <= total_price) OR (:type = 'RENT' AND CAST(CAST(:priceFrom AS TEXT) AS DOUBLE PRECISION) <= price_rent))\n" +
            " AND (:priceTo is null OR (:type = 'BUY' AND CAST(CAST(:priceTo AS TEXT) AS DOUBLE PRECISION) >= total_price) OR (:type = 'RENT' AND CAST(CAST(:priceTo AS TEXT) AS DOUBLE PRECISION) >= price_rent))\n"
            , nativeQuery = true)
    List<Apartment> findAllByStatusAndPriceBetweenAndAreaBetween(String status, String type, Double priceFrom, Double priceTo, Double areaFrom, Double areaTo);

    @Query(value = "SELECT a.* FROM apartment a JOIN apartment_address ad ON a.id = ad.id " +
            " WHERE a.status = :status" +
            " AND a.type_apartment = :type" +
            " AND ad.province_id = :provinceId" +
            " AND (:areaFrom is null OR CAST(CAST(:areaFrom AS TEXT) AS DOUBLE PRECISION) <= a.area)" +
            " AND (:areaTo is null OR CAST(CAST(:areaTo AS TEXT) AS DOUBLE PRECISION) >= a.area)" +
            " AND (:priceFrom is null OR (:type = 'BUY' AND CAST(CAST(:priceFrom AS TEXT) AS DOUBLE PRECISION) <= a.total_price) OR (:type = 'RENT' AND CAST(CAST(:priceFrom AS TEXT) AS DOUBLE PRECISION) <= a.price_rent))\n" +
            " AND (:priceTo is null OR (:type = 'BUY' AND CAST(CAST(:priceTo AS TEXT) AS DOUBLE PRECISION) >= a.total_price) OR (:type = 'RENT' AND CAST(CAST(:priceTo AS TEXT) AS DOUBLE PRECISION) >= a.price_rent))\n"
            , nativeQuery = true)
    List<Apartment> findAllByStatusAndDistrictAndPriceBetweenAndAreaBetween(String status, Long provinceId, String type, Double priceFrom, Double priceTo, Double areaFrom, Double areaTo);

    Page<Apartment> findAllByAuthorIdAndStatusIn(Long userId, List<EApartmentStatus> status, Pageable pageable);

    Page<Apartment> findAllByStatusAndTitleContainingIgnoreCase(EApartmentStatus status, String title, Pageable pageable);

    @Query(value = "WITH tc_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as tc_total FROM tracking_category WHERE ip = :ip OR user_id = :userId),\n" +
            "tc AS (SELECT category_id, SUM(rating) as tc_item FROM tracking_category WHERE ip = :ip OR user_id = :userId GROUP BY category_id),\n" +
            "tp_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as tp_total FROM tracking_province WHERE ip = :ip OR user_id = :userId),\n" +
            "tp AS (SELECT province_id, SUM(rating) as tp_item FROM tracking_province WHERE ip = :ip OR user_id = :userId GROUP BY province_id),\n" +
            "td_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as td_total FROM tracking_district WHERE ip = :ip OR user_id = :userId),\n" +
            "td AS (SELECT district_id, SUM(rating) as td_item FROM tracking_district WHERE ip = :ip OR user_id = :userId GROUP BY district_id),\n" +
            "tbath_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as tbath_total FROM tracking_bathroom WHERE ip = :ip OR user_id = :userId),\n" +
            "tbath AS (SELECT bathroom, SUM(rating) as tbath_item FROM tracking_bathroom WHERE ip = :ip OR user_id = :userId GROUP BY bathroom),\n" +
            "tbed_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as tbed_total FROM tracking_bedroom WHERE ip = :ip OR user_id = :userId),\n" +
            "tbed AS (SELECT bedroom, SUM(rating) as tbed_item FROM tracking_bedroom WHERE ip = :ip OR user_id = :userId GROUP BY bedroom),\n" +
            "tf_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as tf_total FROM tracking_floor WHERE ip = :ip OR user_id = :userId),\n" +
            "tf AS (SELECT floor, SUM(rating) as tf_item FROM tracking_floor WHERE ip = :ip OR user_id = :userId GROUP BY floor),\n" +
            "tt_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as tt_total FROM tracking_toilet WHERE ip = :ip OR user_id = :userId),\n" +
            "tt AS (SELECT toilet, SUM(rating) as tt_item FROM tracking_toilet WHERE ip = :ip OR user_id = :userId GROUP BY toilet),\n" +
            "tarea_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as tarea_total FROM tracking_area WHERE ip = :ip OR user_id = :userId),\n" +
            "tarea AS (SELECT area, SUM(rating) as tarea_item FROM tracking_area WHERE ip = :ip OR user_id = :userId GROUP BY area),\n" +
            "tprice_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as tprice_total FROM tracking_price WHERE ip = :ip OR user_id = :userId),\n" +
            "tprice AS (SELECT price, SUM(rating) as tprice_item FROM tracking_price WHERE ip = :ip OR user_id = :userId GROUP BY price),\n" +
            "tdirec_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as tdirec_total FROM tracking_direction WHERE ip = :ip OR user_id = :userId),\n" +
            "tdirec AS (SELECT direction, SUM(rating) as tdirec_item FROM tracking_direction WHERE ip = :ip OR user_id = :userId GROUP BY direction),\n" +
            "tta_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as tta_total FROM tracking_type_apartment WHERE ip = :ip OR user_id = :userId),\n" +
            "tta AS (SELECT type_apartment, SUM(rating) as tta_item FROM tracking_type_apartment WHERE ip = :ip OR user_id = :userId GROUP BY type_apartment)\n" +
            "   SELECT ap.*, \n" +
            "           c.id as categoryId, c.name as categoryName,\n" +
            "           ap.type_apartment as typeApartment,\n" +
            "           ad.bedroom_quantity as bedroomQuantity, ad.bathroom_quantity as bathroomQuantity, ad.floor_quantity as floorQuantity,\n" +
            "           ad.toilet_quantity as toiletQuantity, ad.house_direction as houseDirection,\n" +
            "           add.district_id as districtId,\n" +
            "           add.province_id as provinceId,\n" +
            "           add.address as address,\n" +
            "           ap.author_id as authorId,\n" +
            "           ap.created_at as createdAt,\n" +
            "           (((CASE WHEN tc.tc_item is null THEN 0 ELSE tc.tc_item END)/tc_total.tc_total) \n" +
            "               + ((CASE WHEN tp.tp_item is null THEN 0 ELSE tp.tp_item END)/tp_total.tp_total) \n" +
            "               + ((CASE WHEN td.td_item is null THEN 0 ELSE td.td_item END)/td_total.td_total)\n" +
            "               + ((CASE WHEN tbath.tbath_item is null THEN 0 ELSE tbath.tbath_item END)/tbath_total.tbath_total) \n" +
            "               + ((CASE WHEN tbed.tbed_item is null THEN 0 ELSE tbed.tbed_item END)/tbed_total.tbed_total) \n" +
            "               + ((CASE WHEN tf.tf_item is null THEN 0 ELSE tf.tf_item END)/tf_total.tf_total)\n" +
            "               + ((CASE WHEN tt.tt_item is null THEN 0 ELSE tt.tt_item END)/tt_total.tt_total)\n" +
            "               + ((CASE WHEN tarea.tarea_item is null THEN 0 ELSE tarea.tarea_item END)/tarea_total.tarea_total) \n" +
            "               + ((CASE WHEN tprice.tprice_item is null THEN 0 ELSE tprice.tprice_item END)/tprice_total.tprice_total)\n" +
            "               + ((CASE WHEN tta.tta_item is null THEN 0 ELSE tta.tta_item END)/tta_total.tta_total)\n" +
            "               + ((CASE WHEN tdirec.tdirec_item is null THEN 0 ELSE tdirec.tdirec_item END)/tdirec_total.tdirec_total)) as rating\n" +
            "   FROM apartment ap\n" +
            "           JOIN apartment_address add ON add.id = ap.id\n" +
            "           JOIN apartment_detail ad ON ad.id = ap.id\n" +
            "           JOIN category c ON c.id = ap.category_id\n" +
            "           JOIN tc_total ON 1=1 LEFT JOIN tc ON tc.category_id = ap.category_id\n" +
            "           JOIN tp_total ON 1=1 LEFT JOIN tp ON tp.province_id = add.province_id\n" +
            "           JOIN td_total ON 1=1 LEFT JOIN td ON td.district_id = add.district_id\n" +
            "           JOIN tbath_total ON 1=1 LEFT JOIN tbath ON tbath.bathroom = ad.bathroom_quantity\n" +
            "           JOIN tbed_total ON 1=1 LEFT JOIN tbed ON tbed.bedroom = ad.bedroom_quantity\n" +
            "           JOIN tf_total ON 1=1 LEFT JOIN tf ON tf.floor = ad.floor_quantity\n" +
            "           JOIN tt_total ON 1=1 LEFT JOIN tt ON tt.toilet = ad.toilet_quantity\n" +
            "           JOIN tarea_total ON 1=1 LEFT JOIN tarea ON tarea.area = ap.area\n" +
            "           JOIN tprice_total ON 1=1 LEFT JOIN tprice ON tprice.price = ap.total_price OR tprice.price = ap.price_rent\n" +
            "           JOIN tdirec_total ON 1=1 LEFT JOIN tdirec ON tdirec.direction = ad.house_direction\n" +
            "           JOIN tta_total ON 1=1 LEFT JOIN tta ON tta.type_apartment = ap.type_apartment\n" +
            "   WHERE (:#{#param.apartmentStatus} is null OR ap.status = CAST(:#{#param.apartmentStatus} AS TEXT)) \n" +
            "            AND (:#{#param.districtId} is null OR add.district_id = CAST(CAST(:#{#param.districtId} AS TEXT) AS BIGINT)) \n" +
            "            AND (:#{#param.provinceId} is null OR add.province_id = CAST(CAST(:#{#param.provinceId} AS TEXT) AS BIGINT)) \n" +
            "            AND (:#{#param.priceFrom} is null OR (:#{#param.typeApartment} = 'BUY' AND CAST(CAST(:#{#param.priceFrom} AS TEXT) AS DOUBLE PRECISION) <= ap.total_price) OR (:#{#param.typeApartment} = 'RENT' AND CAST(CAST(:#{#param.priceFrom} AS TEXT) AS DOUBLE PRECISION) <= ap.price_rent))\n" +
            "            AND (:#{#param.priceTo} is null OR (:#{#param.typeApartment} = 'BUY' AND CAST(CAST(:#{#param.priceTo} AS TEXT) AS DOUBLE PRECISION) >= ap.total_price) OR (:#{#param.typeApartment} = 'RENT' AND CAST(CAST(:#{#param.priceTo} AS TEXT) AS DOUBLE PRECISION) >= ap.price_rent))\n" +
            "            AND (:#{#param.areaFrom} is null OR ap.area >= CAST(CAST(:#{#param.areaFrom} AS TEXT) AS DOUBLE PRECISION))\n" +
            "            AND (:#{#param.areaTo} is null OR ap.area <= CAST(CAST(:#{#param.areaTo} AS TEXT) AS DOUBLE PRECISION))\n" +
            "            AND (:#{#param.categoryId} is null OR ap.category_id = CAST(CAST(:#{#param.categoryId} AS TEXT) AS BIGINT))\n" +
            "            AND (:#{#param.typeApartment} is null OR ap.type_apartment = CAST(:#{#param.typeApartment} AS TEXT))\n" +
            "            AND (:#{#param.search} is null OR ap.title LIKE CAST(:#{#param.search} AS TEXT))\n" +
            "            AND (:#{#param.houseDirection} is null OR ad.house_direction LIKE CAST(:#{#param.houseDirection} AS TEXT))\n" +
            "            AND (:#{#param.bedroomQuantity} is null OR ad.bedroom_quantity = CAST(CAST(:#{#param.bedroomQuantity} AS TEXT) AS BIGINT))\n" +
            "            AND (:#{#param.bathroomQuantity} is null OR ad.bathroom_quantity = CAST(CAST(:#{#param.bathroomQuantity} AS TEXT) AS BIGINT))\n" +
            "            AND (:#{#param.floorQuantity} is null OR ad.floor_quantity = CAST(CAST(:#{#param.floorQuantity} AS TEXT) AS BIGINT))\n" +
            "            AND (:#{#param.highlight} is null OR ap.highlight = CAST(CAST(:#{#param.highlight} AS CHARACTER VARYING) AS BOOLEAN)) \n",
            nativeQuery = true)
    List<ApartmentRating> findRecommendApartmentByUserIdAndIp(@Param("param") SearchApartmentRequest param, @Param("userId") Long userId, @Param("ip") String ip, Pageable pageable);

    @Query(value = "select count(ap.*) from apartment ap, apartment_address add, apartment_detail ad" +
            "           WHERE ap.id = ad.id AND ap.id = add.id " +
            "            AND (:#{#param.apartmentStatus} is null OR ap.status = CAST(:#{#param.apartmentStatus} AS TEXT)) \n" +
            "            AND (:#{#param.districtId} is null OR add.district_id = CAST(CAST(:#{#param.districtId} AS TEXT) AS BIGINT)) \n" +
            "            AND (:#{#param.provinceId} is null OR add.province_id = CAST(CAST(:#{#param.provinceId} AS TEXT) AS BIGINT)) \n" +
            "            AND (:#{#param.priceFrom} is null OR (:#{#param.typeApartment} = 'BUY' AND CAST(CAST(:#{#param.priceFrom} AS TEXT) AS DOUBLE PRECISION) <= ap.total_price) OR (:#{#param.typeApartment} = 'RENT' AND CAST(CAST(:#{#param.priceFrom} AS TEXT) AS DOUBLE PRECISION) <= ap.price_rent))\n" +
            "            AND (:#{#param.priceTo} is null OR (:#{#param.typeApartment} = 'BUY' AND CAST(CAST(:#{#param.priceTo} AS TEXT) AS DOUBLE PRECISION) >= ap.total_price) OR (:#{#param.typeApartment} = 'RENT' AND CAST(CAST(:#{#param.priceTo} AS TEXT) AS DOUBLE PRECISION) >= ap.price_rent))\n" +
            "            AND (:#{#param.areaFrom} is null OR ap.area >= CAST(CAST(:#{#param.areaFrom} AS TEXT) AS DOUBLE PRECISION))\n" +
            "            AND (:#{#param.areaTo} is null OR ap.area <= CAST(CAST(:#{#param.areaTo} AS TEXT) AS DOUBLE PRECISION))\n" +
            "            AND (:#{#param.categoryId} is null OR ap.category_id = CAST(CAST(:#{#param.categoryId} AS TEXT) AS BIGINT))\n" +
            "            AND (:#{#param.typeApartment} is null OR ap.type_apartment = CAST(:#{#param.typeApartment} AS TEXT))\n" +
            "            AND (:#{#param.search} is null OR ap.title LIKE CAST(:#{#param.search} AS TEXT))\n" +
            "            AND (:#{#param.houseDirection} is null OR ad.house_direction LIKE CAST(:#{#param.houseDirection} AS TEXT))\n" +
            "            AND (:#{#param.bedroomQuantity} is null OR ad.bedroom_quantity = CAST(CAST(:#{#param.bedroomQuantity} AS TEXT) AS BIGINT))\n" +
            "            AND (:#{#param.bathroomQuantity} is null OR ad.bathroom_quantity = CAST(CAST(:#{#param.bathroomQuantity} AS TEXT) AS BIGINT))\n" +
            "            AND (:#{#param.floorQuantity} is null OR ad.floor_quantity = CAST(CAST(:#{#param.floorQuantity} AS TEXT) AS BIGINT)) \n" +
            "            AND (:#{#param.highlight} is null OR ap.highlight = CAST(CAST(:#{#param.highlight} AS CHARACTER VARYING) AS BOOLEAN)) \n",
            nativeQuery = true)
    Long countRecommendApartmentByUserIdAndIp(@Param("param") SearchApartmentRequest param);

    @Query(value = "WITH tc_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as tc_total FROM tracking_category WHERE ip = :ip OR user_id = :userId),\n" +
            "tc AS (SELECT category_id, SUM(rating) as tc_item FROM tracking_category WHERE ip = :ip OR user_id = :userId GROUP BY category_id),\n" +
            "tp_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as tp_total FROM tracking_province WHERE ip = :ip OR user_id = :userId),\n" +
            "tp AS (SELECT province_id, SUM(rating) as tp_item FROM tracking_province WHERE ip = :ip OR user_id = :userId GROUP BY province_id),\n" +
            "td_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as td_total FROM tracking_district WHERE ip = :ip OR user_id = :userId),\n" +
            "td AS (SELECT district_id, SUM(rating) as td_item FROM tracking_district WHERE ip = :ip OR user_id = :userId GROUP BY district_id),\n" +
            "tbath_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as tbath_total FROM tracking_bathroom WHERE ip = :ip OR user_id = :userId),\n" +
            "tbath AS (SELECT bathroom, SUM(rating) as tbath_item FROM tracking_bathroom WHERE ip = :ip OR user_id = :userId GROUP BY bathroom),\n" +
            "tbed_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as tbed_total FROM tracking_bedroom WHERE ip = :ip OR user_id = :userId),\n" +
            "tbed AS (SELECT bedroom, SUM(rating) as tbed_item FROM tracking_bedroom WHERE ip = :ip OR user_id = :userId GROUP BY bedroom),\n" +
            "tf_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as tf_total FROM tracking_floor WHERE ip = :ip OR user_id = :userId),\n" +
            "tf AS (SELECT floor, SUM(rating) as tf_item FROM tracking_floor WHERE ip = :ip OR user_id = :userId GROUP BY floor),\n" +
            "tt_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as tt_total FROM tracking_toilet WHERE ip = :ip OR user_id = :userId),\n" +
            "tt AS (SELECT toilet, SUM(rating) as tt_item FROM tracking_toilet WHERE ip = :ip OR user_id = :userId GROUP BY toilet),\n" +
            "tarea_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as tarea_total FROM tracking_area WHERE ip = :ip OR user_id = :userId),\n" +
            "tarea AS (SELECT area, SUM(rating) as tarea_item FROM tracking_area WHERE ip = :ip OR user_id = :userId GROUP BY area),\n" +
            "tprice_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as tprice_total FROM tracking_price WHERE ip = :ip OR user_id = :userId),\n" +
            "tprice AS (SELECT price, SUM(rating) as tprice_item FROM tracking_price WHERE ip = :ip OR user_id = :userId GROUP BY price),\n" +
            "tdirec_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as tdirec_total FROM tracking_direction WHERE ip = :ip OR user_id = :userId),\n" +
            "tdirec AS (SELECT direction, SUM(rating) as tdirec_item FROM tracking_direction WHERE ip = :ip OR user_id = :userId GROUP BY direction),\n" +
            "tta_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as tta_total FROM tracking_type_apartment WHERE ip = :ip OR user_id = :userId),\n" +
            "tta AS (SELECT type_apartment, SUM(rating) as tta_item FROM tracking_type_apartment WHERE ip = :ip OR user_id = :userId GROUP BY type_apartment)\n" +
            "   SELECT ap.*, \n" +
            "           c.id as categoryId, c.name as categoryName,\n" +
            "           ap.type_apartment as typeApartment,\n" +
            "           ad.bedroom_quantity as bedroomQuantity, ad.bathroom_quantity as bathroomQuantity, ad.floor_quantity as floorQuantity,\n" +
            "           ad.toilet_quantity as toiletQuantity, ad.house_direction as houseDirection,\n" +
            "           add.district_id as districtId,\n" +
            "           add.province_id as provinceId,\n" +
            "           add.address as address,\n" +
            "           ap.author_id as authorId,\n" +
            "           (((CASE WHEN tc.tc_item is null THEN 0 ELSE tc.tc_item END)/tc_total.tc_total) \n" +
            "               + ((CASE WHEN tp.tp_item is null THEN 0 ELSE tp.tp_item END)/tp_total.tp_total) \n" +
            "               + ((CASE WHEN td.td_item is null THEN 0 ELSE td.td_item END)/td_total.td_total)\n" +
            "               + ((CASE WHEN tbath.tbath_item is null THEN 0 ELSE tbath.tbath_item END)/tbath_total.tbath_total) \n" +
            "               + ((CASE WHEN tbed.tbed_item is null THEN 0 ELSE tbed.tbed_item END)/tbed_total.tbed_total) \n" +
            "               + ((CASE WHEN tf.tf_item is null THEN 0 ELSE tf.tf_item END)/tf_total.tf_total)\n" +
            "               + ((CASE WHEN tt.tt_item is null THEN 0 ELSE tt.tt_item END)/tt_total.tt_total)\n" +
            "               + ((CASE WHEN tarea.tarea_item is null THEN 0 ELSE tarea.tarea_item END)/tarea_total.tarea_total) \n" +
            "               + ((CASE WHEN tprice.tprice_item is null THEN 0 ELSE tprice.tprice_item END)/tprice_total.tprice_total)\n" +
            "               + ((CASE WHEN tta.tta_item is null THEN 0 ELSE tta.tta_item END)/tta_total.tta_total)\n" +
            "               + ((CASE WHEN tdirec.tdirec_item is null THEN 0 ELSE tdirec.tdirec_item END)/tdirec_total.tdirec_total)) as rating\n" +
            "   FROM apartment ap\n" +
            "           JOIN apartment_address add ON add.id = ap.id\n" +
            "           JOIN apartment_detail ad ON ad.id = ap.id\n" +
            "           JOIN category c ON c.id = ap.category_id\n" +
            "           JOIN tc_total ON 1=1 LEFT JOIN tc ON tc.category_id = ap.category_id\n" +
            "           JOIN tp_total ON 1=1 LEFT JOIN tp ON tp.province_id = add.province_id\n" +
            "           JOIN td_total ON 1=1 LEFT JOIN td ON td.district_id = add.district_id\n" +
            "           JOIN tbath_total ON 1=1 LEFT JOIN tbath ON tbath.bathroom = ad.bathroom_quantity\n" +
            "           JOIN tbed_total ON 1=1 LEFT JOIN tbed ON tbed.bedroom = ad.bedroom_quantity\n" +
            "           JOIN tf_total ON 1=1 LEFT JOIN tf ON tf.floor = ad.floor_quantity\n" +
            "           JOIN tt_total ON 1=1 LEFT JOIN tt ON tt.toilet = ad.toilet_quantity\n" +
            "           JOIN tarea_total ON 1=1 LEFT JOIN tarea ON tarea.area = ap.area\n" +
            "           JOIN tprice_total ON 1=1 LEFT JOIN tprice ON tprice.price = ap.total_price OR tprice.price = ap.price_rent\n" +
            "           JOIN tdirec_total ON 1=1 LEFT JOIN tdirec ON tdirec.direction = ad.house_direction\n" +
            "           JOIN tta_total ON 1=1 LEFT JOIN tta ON tta.type_apartment = ap.type_apartment\n" +
            "   WHERE ap.id = :id\n",
            nativeQuery = true)
    List<ApartmentRating> findApartmentDetailRatingByUserIdAndIp(Long id, @Param("userId") Long userId, @Param("ip") String ip);

    @Query(value = "WITH tc_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as tc_total FROM tracking_category WHERE ip = :ip OR user_id = :userId),\n" +
            "tc AS (SELECT category_id, SUM(rating) as tc_item FROM tracking_category WHERE ip = :ip OR user_id = :userId GROUP BY category_id),\n" +
            "tp_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as tp_total FROM tracking_province WHERE ip = :ip OR user_id = :userId),\n" +
            "tp AS (SELECT province_id, SUM(rating) as tp_item FROM tracking_province WHERE ip = :ip OR user_id = :userId GROUP BY province_id),\n" +
            "td_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as td_total FROM tracking_district WHERE ip = :ip OR user_id = :userId),\n" +
            "td AS (SELECT district_id, SUM(rating) as td_item FROM tracking_district WHERE ip = :ip OR user_id = :userId GROUP BY district_id),\n" +
            "tbath_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as tbath_total FROM tracking_bathroom WHERE ip = :ip OR user_id = :userId),\n" +
            "tbath AS (SELECT bathroom, SUM(rating) as tbath_item FROM tracking_bathroom WHERE ip = :ip OR user_id = :userId GROUP BY bathroom),\n" +
            "tbed_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as tbed_total FROM tracking_bedroom WHERE ip = :ip OR user_id = :userId),\n" +
            "tbed AS (SELECT bedroom, SUM(rating) as tbed_item FROM tracking_bedroom WHERE ip = :ip OR user_id = :userId GROUP BY bedroom),\n" +
            "tf_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as tf_total FROM tracking_floor WHERE ip = :ip OR user_id = :userId),\n" +
            "tf AS (SELECT floor, SUM(rating) as tf_item FROM tracking_floor WHERE ip = :ip OR user_id = :userId GROUP BY floor),\n" +
            "tt_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as tt_total FROM tracking_toilet WHERE ip = :ip OR user_id = :userId),\n" +
            "tt AS (SELECT toilet, SUM(rating) as tt_item FROM tracking_toilet WHERE ip = :ip OR user_id = :userId GROUP BY toilet),\n" +
            "tarea_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as tarea_total FROM tracking_area WHERE ip = :ip OR user_id = :userId),\n" +
            "tarea AS (SELECT area, SUM(rating) as tarea_item FROM tracking_area WHERE ip = :ip OR user_id = :userId GROUP BY area),\n" +
            "tprice_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as tprice_total FROM tracking_price WHERE ip = :ip OR user_id = :userId),\n" +
            "tprice AS (SELECT price, SUM(rating) as tprice_item FROM tracking_price WHERE ip = :ip OR user_id = :userId GROUP BY price),\n" +
            "tdirec_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as tdirec_total FROM tracking_direction WHERE ip = :ip OR user_id = :userId),\n" +
            "tdirec AS (SELECT direction, SUM(rating) as tdirec_item FROM tracking_direction WHERE ip = :ip OR user_id = :userId GROUP BY direction),\n" +
            "tta_total AS (SELECT (CASE WHEN SUM(rating) is null THEN 1 ELSE SUM(rating) END) as tta_total FROM tracking_type_apartment WHERE ip = :ip OR user_id = :userId),\n" +
            "tta AS (SELECT type_apartment, SUM(rating) as tta_item FROM tracking_type_apartment WHERE ip = :ip OR user_id = :userId GROUP BY type_apartment)\n" +
            "   SELECT ap.*, \n" +
            "           c.id as categoryId, c.name as categoryName,\n" +
            "           ap.type_apartment as typeApartment,\n" +
            "           ad.bedroom_quantity as bedroomQuantity, ad.bathroom_quantity as bathroomQuantity, ad.floor_quantity as floorQuantity,\n" +
            "           ad.toilet_quantity as toiletQuantity, ad.house_direction as houseDirection,\n" +
            "           add.district_id as districtId,\n" +
            "           add.province_id as provinceId,\n" +
            "           add.address as address,\n" +
            "           ap.author_id as authorId,\n" +
            "           ap.created_at as createdAt,\n" +
            "           (((CASE WHEN tc.tc_item is null THEN 0 ELSE tc.tc_item END)/tc_total.tc_total) \n" +
            "               + ((CASE WHEN tp.tp_item is null THEN 0 ELSE tp.tp_item END)/tp_total.tp_total) \n" +
            "               + ((CASE WHEN td.td_item is null THEN 0 ELSE td.td_item END)/td_total.td_total)\n" +
            "               + ((CASE WHEN tbath.tbath_item is null THEN 0 ELSE tbath.tbath_item END)/tbath_total.tbath_total) \n" +
            "               + ((CASE WHEN tbed.tbed_item is null THEN 0 ELSE tbed.tbed_item END)/tbed_total.tbed_total) \n" +
            "               + ((CASE WHEN tf.tf_item is null THEN 0 ELSE tf.tf_item END)/tf_total.tf_total)\n" +
            "               + ((CASE WHEN tt.tt_item is null THEN 0 ELSE tt.tt_item END)/tt_total.tt_total)\n" +
            "               + ((CASE WHEN tarea.tarea_item is null THEN 0 ELSE tarea.tarea_item END)/tarea_total.tarea_total) \n" +
            "               + ((CASE WHEN tprice.tprice_item is null THEN 0 ELSE tprice.tprice_item END)/tprice_total.tprice_total)\n" +
            "               + ((CASE WHEN tta.tta_item is null THEN 0 ELSE tta.tta_item END)/tta_total.tta_total)\n" +
            "               + ((CASE WHEN tdirec.tdirec_item is null THEN 0 ELSE tdirec.tdirec_item END)/tdirec_total.tdirec_total)) as rating\n" +
            "   FROM apartment ap\n" +
            "           JOIN apartment_address add ON add.id = ap.id\n" +
            "           JOIN apartment_detail ad ON ad.id = ap.id\n" +
            "           JOIN category c ON c.id = ap.category_id\n" +
            "           JOIN tc_total ON 1=1 LEFT JOIN tc ON tc.category_id = ap.category_id\n" +
            "           JOIN tp_total ON 1=1 LEFT JOIN tp ON tp.province_id = add.province_id\n" +
            "           JOIN td_total ON 1=1 LEFT JOIN td ON td.district_id = add.district_id\n" +
            "           JOIN tbath_total ON 1=1 LEFT JOIN tbath ON tbath.bathroom = ad.bathroom_quantity\n" +
            "           JOIN tbed_total ON 1=1 LEFT JOIN tbed ON tbed.bedroom = ad.bedroom_quantity\n" +
            "           JOIN tf_total ON 1=1 LEFT JOIN tf ON tf.floor = ad.floor_quantity\n" +
            "           JOIN tt_total ON 1=1 LEFT JOIN tt ON tt.toilet = ad.toilet_quantity\n" +
            "           JOIN tarea_total ON 1=1 LEFT JOIN tarea ON tarea.area = ap.area\n" +
            "           JOIN tprice_total ON 1=1 LEFT JOIN tprice ON tprice.price = ap.total_price OR tprice.price = ap.price_rent\n" +
            "           JOIN tdirec_total ON 1=1 LEFT JOIN tdirec ON tdirec.direction = ad.house_direction\n" +
            "           JOIN tta_total ON 1=1 LEFT JOIN tta ON tta.type_apartment = ap.type_apartment\n" +
            "   WHERE  (COALESCE(:#{#param.bathrooms}) is null OR ad.bathroom_quantity IN :#{#param.bathrooms} ) " +
            "           AND (COALESCE(:#{#param.bedrooms}) is null OR ad.bedroom_quantity IN :#{#param.bedrooms} ) " +
            "           AND (COALESCE(:#{#param.categories}) is null OR ap.category_id IN :#{#param.categories} ) " +
            "           AND (COALESCE(:#{#param.directions}) is null OR ad.house_direction IN :#{#param.directions} ) " +
            "           AND (COALESCE(:#{#param.districts}) is null OR add.district_id IN :#{#param.districts} )" +
            "           AND (COALESCE(:#{#param.provinces}) is null OR add.province_id IN :#{#param.provinces} )" +
            "           AND (COALESCE(:#{#param.floors}) is null OR ad.floor_quantity IN :#{#param.floors} )" +
            "           AND (:#{#param.areaLow} is null OR ap.area BETWEEN CAST(CAST(:#{#param.areaLow} AS TEXT) AS DOUBLE PRECISION) AND CAST(CAST(:#{#param.areaHigh} AS TEXT) AS DOUBLE PRECISION)) " +
            "           AND (:#{#param.type} is null OR ap.type_apartment = CAST(:#{#param.type} AS TEXT)) " +
            "           AND (:#{#param.priceLow} is null " +
            "               OR ((:#{#param.type} is null) AND (ap.total_price BETWEEN CAST(CAST(:#{#param.priceLow} AS TEXT) AS DOUBLE PRECISION) AND CAST(CAST(:#{#param.priceHigh} AS TEXT) AS DOUBLE PRECISION)) OR (ap.price_rent BETWEEN CAST(CAST(:#{#param.priceLow} AS TEXT) AS DOUBLE PRECISION) AND CAST(CAST(:#{#param.priceHigh} AS TEXT) AS DOUBLE PRECISION)))" +
            "               OR (:#{#param.type} = 'BUY' AND ap.total_price BETWEEN CAST(CAST(:#{#param.priceLow} AS TEXT) AS DOUBLE PRECISION) AND CAST(CAST(:#{#param.priceHigh} AS TEXT) AS DOUBLE PRECISION)) " +
            "               OR (:#{#param.type} = 'RENT' AND ap.price_rent BETWEEN CAST(CAST(:#{#param.priceLow} AS TEXT) AS DOUBLE PRECISION) AND CAST(CAST(:#{#param.priceHigh} AS TEXT) AS DOUBLE PRECISION))) " +
            "   ORDER BY rating desc LIMIT :size OFFSET :page",
            nativeQuery = true)
    List<ApartmentRating> findSuitableApartmentForChatBox(@Param("param") ApartmentQueryParam param, Long userId, String ip, Integer page, Integer size);
}
