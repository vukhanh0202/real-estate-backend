package com.uit.realestate.domain.house;

import com.uit.realestate.domain.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "house_address")
public class HouseAddress {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id",foreignKey = @ForeignKey(name = "none"))
    private House house;

    private String countryCode;
    private String countryName;
    private Long provinceId;
    private String provinceName;
    private Long districtId;
    private String districtName;
    private String address;

}
