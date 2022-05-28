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
            countQuery = "select count(ap.*) from apartment ap, apartment_address add, apartment_detail ad" +
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
                    "            AND (:#{#param.floorQuantity} is null OR ad.floor_quantity = CAST(CAST(:#{#param.floorQuantity} AS TEXT) AS BIGINT)) \n"+
                    "            AND (:#{#param.highlight} is null OR ap.highlight = CAST(CAST(:#{#param.highlight} AS CHARACTER VARYING) AS BOOLEAN)) \n",
            nativeQuery = true)
    Page<ApartmentRating> findRecommendApartmentByUserIdAndIp(@Param("param") SearchApartmentRequest param, @Param("userId") Long userId, @Param("ip") String ip, Pageable pageable);

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

    @Query(value = "SELECT ap.*, (SUM(COALESCE(tc.rating, 0)) + SUM(COALESCE(tp.rating, 0)) + SUM(COALESCE(tta.rating, 0)) + SUM(COALESCE(td.rating, 0))) as rating\n" +
            " FROM apartment ap " +
            " JOIN apartment_address ad ON ap.id = ad.id " +
            " JOIN apartment_detail adt ON ap.id = adt.id " +
            " FULL OUTER JOIN tracking_category tc ON tc.category_id = ap.category_id " +
            " FULL OUTER JOIN tracking_province tp ON tp.province_id = ad.province_id " +
            " FULL OUTER JOIN tracking_district td ON td.district_id = ad.district_id " +
            " FULL OUTER JOIN tracking_type_apartment tta ON tta.type_apartment = ap.type_apartment " +
            " WHERE ((tc.ip = :ip OR tc.user_id = :userId) " +
            "           OR (tp.ip = :ip OR tp.user_id = :userId) " +
            "           OR (tta.ip = :ip OR tta.user_id = :userId) " +
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
            "           OR ((:#{#param.type} is null) AND (ap.total_price BETWEEN CAST(CAST(:#{#param.priceLow} AS TEXT) AS DOUBLE PRECISION) AND CAST(CAST(:#{#param.priceHigh} AS TEXT) AS DOUBLE PRECISION)) OR (ap.price_rent BETWEEN CAST(CAST(:#{#param.priceLow} AS TEXT) AS DOUBLE PRECISION) AND CAST(CAST(:#{#param.priceHigh} AS TEXT) AS DOUBLE PRECISION)))" +
            "           OR (:#{#param.type} = 'BUY' AND ap.total_price BETWEEN CAST(CAST(:#{#param.priceLow} AS TEXT) AS DOUBLE PRECISION) AND CAST(CAST(:#{#param.priceHigh} AS TEXT) AS DOUBLE PRECISION)) " +
            "           OR (:#{#param.type} = 'RENT' AND ap.price_rent BETWEEN CAST(CAST(:#{#param.priceLow} AS TEXT) AS DOUBLE PRECISION) AND CAST(CAST(:#{#param.priceHigh} AS TEXT) AS DOUBLE PRECISION))) " +
            " GROUP BY ap.id " +
            " ORDER BY rating DESC, ap.created_at DESC ",
            nativeQuery = true)
    Page<Apartment> findRecommendApartmentForChatBox(@Param("param") ApartmentQueryParam param, Long userId, String ip, Pageable pageable);

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
            "), type_apartment_tb AS (\n" +
            "   SELECT a.id, a.title,  \n" +
            "       SUM(CASE\n" +
            "           WHEN u.type_apartment is null THEN 0\n" +
            "           ELSE\n" +
            "           CASE\n" +
            "           WHEN a.type_apartment = u.type_apartment THEN :accuracy\n" +
            "           ELSE 0\n" +
            "           END\n" +
            "           END) \n" +
            "       AS suitable,\n" +
            "       SUM(CASE\n" +
            "           WHEN u.type_apartment is null THEN 0\n" +
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
            "       SUM((dtb.suitable + ptb.suitable + ctb.suitable + atb.suitable + ftb.suitable + dtb.suitable + batb.suitable + tatb.suitable)/(dtb.total + ptb.total + ctb.total + atb.total + ftb.total + dtb.total + batb.total + tatb.total))*100 AS suitable_rate\n" +
            "FROM district_tb dtb \n" +
            "JOIN province_tb ptb ON dtb.id = ptb.id\n" +
            "JOIN category_tb ctb ON dtb.id = ctb.id\n" +
            "JOIN area_tb atb ON dtb.id = atb.id\n" +
            "JOIN floor_tb ftb ON dtb.id = ftb.id\n" +
            "JOIN bedroom_tb btb ON dtb.id = btb.id\n" +
            "JOIN bathroom_tb batb ON dtb.id = batb.id\n" +
            "JOIN type_apartment_tb tatb ON dtb.id = tatb.id\n" +
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
            "), type_apartment_tb AS (\n" +
            "   SELECT a.id, a.title,  \n" +
            "       SUM(CASE\n" +
            "           WHEN u.type_apartment is null THEN 0\n" +
            "           ELSE\n" +
            "           CASE\n" +
            "           WHEN a.type_apartment = u.type_apartment THEN :accuracy\n" +
            "           ELSE 0\n" +
            "           END\n" +
            "           END) \n" +
            "       AS suitable,\n" +
            "       SUM(CASE\n" +
            "           WHEN u.type_apartment is null THEN 0\n" +
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
            "       SUM((dtb.suitable + ptb.suitable + ctb.suitable + atb.suitable + ftb.suitable + dtb.suitable + batb.suitable + tatb.suitable)/(dtb.total + ptb.total + ctb.total + atb.total + ftb.total + dtb.total + batb.total + tatb.total))*100 AS suitable_rate\n" +
            "FROM district_tb dtb \n" +
            "JOIN province_tb ptb ON dtb.id = ptb.id\n" +
            "JOIN category_tb ctb ON dtb.id = ctb.id\n" +
            "JOIN area_tb atb ON dtb.id = atb.id\n" +
            "JOIN floor_tb ftb ON dtb.id = ftb.id\n" +
            "JOIN bedroom_tb btb ON dtb.id = btb.id\n" +
            "JOIN bathroom_tb batb ON dtb.id = batb.id\n" +
            "JOIN type_apartment_tb tatb ON dtb.id = tatb.id\n" +
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
            "           OR ((:#{#param.type} is null) AND (dtb.total_price BETWEEN CAST(CAST(:#{#param.priceLow} AS TEXT) AS DOUBLE PRECISION) AND CAST(CAST(:#{#param.priceHigh} AS TEXT) AS DOUBLE PRECISION)) OR (dtb.price_rent BETWEEN CAST(CAST(:#{#param.priceLow} AS TEXT) AS DOUBLE PRECISION) AND CAST(CAST(:#{#param.priceHigh} AS TEXT) AS DOUBLE PRECISION)))" +
            "           OR (:#{#param.type} = 'BUY' AND dtb.total_price BETWEEN CAST(CAST(:#{#param.priceLow} AS TEXT) AS DOUBLE PRECISION) AND CAST(CAST(:#{#param.priceHigh} AS TEXT) AS DOUBLE PRECISION)) " +
            "           OR (:#{#param.type} = 'RENT' AND dtb.price_rent BETWEEN CAST(CAST(:#{#param.priceLow} AS TEXT) AS DOUBLE PRECISION) AND CAST(CAST(:#{#param.priceHigh} AS TEXT) AS DOUBLE PRECISION))) " +
            "GROUP BY dtb.id, dtb.area, dtb.author_id, dtb.category_id, dtb.created_at, dtb.created_by, dtb.highlight, dtb.is_deleted, dtb.photos, dtb.price, dtb.price_rent, dtb.status, dtb.title, dtb.total_price, dtb.type_apartment, dtb.unit_rent, dtb.updated_at, dtb.updated_by\n" +
            "ORDER BY suitable_rate desc LIMIT :size OFFSET :page",
            nativeQuery = true)
    List<Apartment> findSuitableApartmentForChatBox(@Param("param") ApartmentQueryParam param, Long accuracy, Long accuracyArea, Long userId, Integer page, Integer size);
}
