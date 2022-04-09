package com.uit.realestate.domain.apartment;

import com.uit.realestate.repository.converter.ListToStringConverter;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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

    private String houseDirection;

    private Integer floorQuantity;

    private Integer bedroomQuantity;

    private Integer bathroomQuantity;

    private Integer toiletQuantity;

    @Column
    @Convert(converter = ListToStringConverter.class)
    private List<String> moreInfo;

}
