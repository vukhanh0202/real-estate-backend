package com.uit.realestate.domain.apartment;

import com.uit.realestate.constant.enums.apartment.EApartmentStatus;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

public interface ApartmentRating{

    Long getId();
    String getTitle();
    Double getArea();
    Double getPrice();
    Double getTotal_Price();
    Double getPrice_Rent();
    String getUnit_Rent();
    Boolean getHighlight();
    String getPhotos();
    String getTypeApartment();
    EApartmentStatus getStatus();
    Long getCategoryId();
    String getCategoryName();
    Long getAuthorId();
    Double getRating();
    Integer getBedroomQuantity();
    Integer getBathroomQuantity();
    Integer getFloorQuantity();
    Integer getToiletQuantity();
    String getDirection();
    Long getDistrictId();
    Long getProvinceId();
    String getAddress();
    @Temporal(TemporalType.TIMESTAMP)
    Date getCreatedAt();

}
