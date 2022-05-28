package com.uit.realestate.domain.tracking;

import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.domain.SqlEntity;
import com.uit.realestate.domain.location.Province;
import com.uit.realestate.domain.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "tracking_type_apartment")
public class TrackingTypeApartment extends SqlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    @Enumerated(EnumType.STRING)
    private ETypeApartment typeApartment;

    @Column
    private String ip;

    @Column
    private Long rating = 0L;

    public TrackingTypeApartment(Long userId, ETypeApartment typeApartment, String ip, Long rating) {
        if (userId != null && userId != -1){
            this.user = new User(userId);
        }
        if (typeApartment != null){
            this.typeApartment = typeApartment;
        }
        if (ip != null){
            this.ip = ip;
        }
        this.rating = rating;
    }
}

