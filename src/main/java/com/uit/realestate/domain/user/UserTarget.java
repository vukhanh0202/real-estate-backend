package com.uit.realestate.domain.user;

import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.domain.SqlEntity;
import com.uit.realestate.domain.location.District;
import com.uit.realestate.domain.location.Province;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_target")
public class UserTarget extends SqlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id")
    private District district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id")
    private Province province;

    private Double price;

    private String houseDirection;

    private Long floorQuantity;

    private Long bedroomQuantity;

    private Long bathroomQuantity;

    @Enumerated(EnumType.STRING)
    private ETypeApartment typeApartment;

    private Double area;

}
