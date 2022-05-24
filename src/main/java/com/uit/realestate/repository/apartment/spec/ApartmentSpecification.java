package com.uit.realestate.repository.apartment.spec;

import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.payload.apartment.ApartmentQueryParam;
import com.uit.realestate.payload.apartment.SearchApartmentRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ApartmentSpecification {

    public static Specification<Apartment> of(SearchApartmentRequest req) {
        return (Specification<Apartment>) (root, query, builder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (req.getDistrictId() != null)
                predicateList.add(builder.equal(root.get("apartmentAddress").get("district").get("id"), req.getDistrictId()));
            if (req.getProvinceId() != null)
                predicateList.add(builder.equal(root.get("apartmentAddress").get("province").get("id"), req.getProvinceId()));
            if (req.getPriceFrom() != null)
                predicateList.add(builder.greaterThanOrEqualTo(root.get("totalPrice"), req.getPriceFrom()));
            if (req.getPriceTo() != null)
                predicateList.add(builder.lessThanOrEqualTo(root.get("totalPrice"), req.getPriceTo()));
            if (req.getAreaFrom() != null)
                predicateList.add(builder.greaterThanOrEqualTo(root.get("area"), req.getAreaFrom()));
            if (req.getAreaTo() != null)
                predicateList.add(builder.lessThanOrEqualTo(root.get("area"), req.getAreaTo()));
            if (req.getCategoryId() != null)
                predicateList.add(builder.equal(root.get("category").get("id"), req.getCategoryId()));
            if (req.getTypeApartment() != null)
                predicateList.add(builder.equal(root.get("typeApartment"), req.getTypeApartment()));
            if (req.getApartmentStatus() != null)
                predicateList.add(builder.equal(root.get("status"), req.getApartmentStatus()));
            if (!req.getSearch().equals(""))
                predicateList.add(builder.like(builder.lower(root.get("title")), "%" + req.getSearch().toLowerCase() + "%"));
            if (req.getBedroomQuantity() != null)
                predicateList.add(builder.equal(root.get("apartmentDetail").get("bedroomQuantity"), req.getBedroomQuantity()));
            if (req.getBathroomQuantity() != null)
                predicateList.add(builder.equal(root.get("apartmentDetail").get("bathroomQuantity"), req.getBathroomQuantity()));
            if (req.getFloorQuantity() != null)
                predicateList.add(builder.equal(root.get("apartmentDetail").get("floorQuantity"), req.getFloorQuantity()));
            return builder.and(predicateList.toArray(new Predicate[0]));
        };
    }

    public static Specification<Apartment> of(ApartmentQueryParam req) {
        return (Specification<Apartment>) (root, query, builder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(builder.equal(root.get("status"), EApartmentStatus.OPEN));

            if (req.getPriceLow() != null)
                predicateList.add(builder.greaterThanOrEqualTo(root.get("totalPrice"), req.getPriceLow()));
            if (req.getPriceHigh() != null)
                predicateList.add(builder.lessThanOrEqualTo(root.get("totalPrice"), req.getPriceHigh()));
            if (req.getAreaLow() != null)
                predicateList.add(builder.greaterThanOrEqualTo(root.get("area"), req.getAreaLow()));
            if (req.getAreaHigh() != null)
                predicateList.add(builder.lessThanOrEqualTo(root.get("area"), req.getAreaHigh()));
            if (req.getType() != null)
                predicateList.add(builder.equal(root.get("typeApartment"), ETypeApartment.valueOf(req.getType())));
            if (!req.getBedrooms().isEmpty())
                predicateList.add(builder.in(root.get("apartmentDetail").get("bedroomQuantity")).value(req.getBedrooms()));
            if (!req.getBathrooms().isEmpty())
                predicateList.add(builder.in(root.get("apartmentDetail").get("bathroomQuantity")).value(req.getBathrooms()));
            if (!req.getCategories().isEmpty())
                predicateList.add(builder.in(root.get("category").get("id")).value(req.getCategories()));
            if (!req.getDirections().isEmpty())
                predicateList.add(builder.in(root.get("apartmentDetail").get("houseDirection")).value(req.getDirections()));
            if (!req.getDistricts().isEmpty())
                predicateList.add(builder.in(root.get("apartmentAddress").get("district").get("id")).value(req.getDistricts()));
            if (!req.getProvinces().isEmpty())
                predicateList.add(builder.in(root.get("apartmentAddress").get("province").get("id")).value(req.getProvinces()));
            if (!req.getFloors().isEmpty())
                predicateList.add(builder.in(root.get("apartmentDetail").get("floorQuantity")).value(req.getFloors()));
            return builder.and(predicateList.toArray(new Predicate[0]));
        };
    }
}
