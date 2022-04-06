package com.uit.realestate.repository.apartment.spec;

import com.uit.realestate.domain.apartment.Apartment;
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
            // Need to search house direction
            final Long defaultQuantity = 3L;
            if (req.getBedroomQuantity() != null){
                if (req.getBedroomQuantity() > defaultQuantity){
                    predicateList.add(builder.greaterThan(root.get("apartmentDetail").get("bedroomQuantity"), defaultQuantity));
                }else{
                    predicateList.add(builder.equal(root.get("apartmentDetail").get("bedroomQuantity"), req.getBedroomQuantity()));
                }
            }
            if (req.getBathroomQuantity() != null){
                if (req.getBathroomQuantity() > defaultQuantity){
                    predicateList.add(builder.greaterThan(root.get("apartmentDetail").get("bathroomQuantity"), defaultQuantity));
                }else{
                    predicateList.add(builder.equal(root.get("apartmentDetail").get("bathroomQuantity"), req.getBathroomQuantity()));
                }
            }
            if (req.getFloorQuantity() != null){
                if (req.getFloorQuantity() > defaultQuantity){
                    predicateList.add(builder.greaterThan(root.get("apartmentDetail").get("floorQuantity"), defaultQuantity));
                }else{
                    predicateList.add(builder.equal(root.get("apartmentDetail").get("floorQuantity"), req.getFloorQuantity()));
                }
            }
            return builder.and(predicateList.toArray(new Predicate[0]));
        };
    }
}
