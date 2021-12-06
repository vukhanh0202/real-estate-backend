package com.uit.realestate.domain.location;

import com.uit.realestate.domain.apartment.ApartmentAddress;
import com.uit.realestate.domain.tracking.TrackingProvince;
import com.uit.realestate.domain.user.UserAddress;
import com.uit.realestate.domain.user.UserTarget;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "province")
public class Province {

    @Id
    private Long id;

    @Column
    private String name;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "is_city")
    private Boolean isCity = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", foreignKey = @javax.persistence.ForeignKey(name = "none"))
    private Country country;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "province")
    private List<District> districts = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "province")
    private List<ApartmentAddress> apartmentAddresses = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "province")
    private List<UserAddress> userAddresses = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "province")
    private List<TrackingProvince> trackingProvinces = new ArrayList<>();

    public Province(Long id) {
        this.id = id;
    }
}
