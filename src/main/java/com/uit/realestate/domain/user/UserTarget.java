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

    private Long district;

    private Long province;

    private Double price;

    private Long floorQuantity;

    private Long bedroomQuantity;

    private Long bathroomQuantity;

    private Double area;

    private Long category;

}
