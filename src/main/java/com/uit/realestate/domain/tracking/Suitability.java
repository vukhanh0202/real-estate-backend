package com.uit.realestate.domain.tracking;

import com.uit.realestate.domain.SqlEntity;
import com.uit.realestate.domain.location.District;
import com.uit.realestate.domain.location.Province;
import com.uit.realestate.domain.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "suitability")
public class Suitability extends SqlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String district;

    private String province;

    private String price;

    private String area;

    private String category;

    private Long floorQuantity;

    private Long bedroomQuantity;

    private Long bathroomQuantity;

}
