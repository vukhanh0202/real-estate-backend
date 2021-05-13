package com.uit.realestate.domain.apartment;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Table(name = "apartment_detail")
@Data
public class ApartmentDetail {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Apartment apartment;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String frontBuilding;

    private String entranceBuilding;

    private String houseDirection;

    private String balconyDirection;

    private Integer floorQuantity;

    private Integer bedroomQuantity;

    private Integer bathroomQuantity;

    private Integer toiletQuantity;

    private String furniture;

}
