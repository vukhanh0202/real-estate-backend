package com.uit.realestate.domain.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_address")
public class UserAddress {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    private String countryCode;
    private String countryName;
    private Long provinceId;
    private String provinceName;
    private Long districtId;
    private String districtName;
    private String address;

}
