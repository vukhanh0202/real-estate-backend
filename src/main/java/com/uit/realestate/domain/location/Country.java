package com.uit.realestate.domain.location;

import com.uit.realestate.domain.apartment.ApartmentAddress;
import com.uit.realestate.domain.user.UserAddress;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "country")
public class Country{

    @Id
    private String code;

    @Column
    private String name;

    @Column(name = "phone_code")
    private Integer phoneCode;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "country")
    private List<Province> provinces = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "country")
    private List<UserAddress> userAddresses = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "country")
    private List<ApartmentAddress> apartmentAddresses = new ArrayList<>();
}

