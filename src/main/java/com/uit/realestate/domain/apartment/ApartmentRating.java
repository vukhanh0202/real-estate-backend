package com.uit.realestate.domain.apartment;

import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;

public interface ApartmentRating{

    Long getId();
    String getTitle();
    Double getArea();
    Double getPrice();
    Double getTotalPrice();
    Double getPriceRent();
    String getUnitRent();
    Boolean getHighLight();
    String getPhotos();
    ETypeApartment getETypeApartment();
    EApartmentStatus getStatus();
    Long getCategoryId();
    User getAuthor();
    Long getRating();
//    ApartmentDetail getApartmentDetail();

    interface ApartmentAddress {
        Long getProvinceId();
        Long getDistrictId();
        String getAddress();
    }
    interface Category {
        String getCategoryName();
    }
    interface User {
        String getUsername();
        String getFullName();
    }
//    interface ApartmentDetail {
//        String getCity();
//    }
}
