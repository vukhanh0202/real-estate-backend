package com.uit.realestate.domain.location;

import com.uit.realestate.domain.apartment.ApartmentAddress;
import com.uit.realestate.domain.tracking.TrackingDistrict;
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
@Table(name = "district")
public class District{

    @Id
    private Long id;

    @Column
    private String name;

    @Column(name = "short_name")
    private String shortName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id")
    private Province province;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "district")
    private List<ApartmentAddress> apartmentAddresses = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "district")
    private List<TrackingDistrict> trackingCountries = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "district")
    private List<UserAddress> userAddresses = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "district")
    private List<UserTarget> userTargets = new ArrayList<>();

    public District(Long id) {
        this.id = id;
    }
}
