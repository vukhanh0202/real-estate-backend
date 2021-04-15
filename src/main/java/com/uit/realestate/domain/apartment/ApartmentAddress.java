package com.uit.realestate.domain.apartment;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "apartment_address")
public class ApartmentAddress {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id",foreignKey = @ForeignKey(name = "none"))
    private Apartment apartment;

    private String countryCode;
    private String countryName;
    private Long provinceId;
    private String provinceName;
    private Long districtId;
    private String districtName;
    private String address;

}
