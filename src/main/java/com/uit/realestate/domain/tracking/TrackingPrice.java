package com.uit.realestate.domain.tracking;

import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.domain.SqlEntity;
import com.uit.realestate.domain.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "tracking_price")
public class TrackingPrice extends SqlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private Double price;

    @Column
    private String ip;

    @Column
    private Long rating = 0L;

    public TrackingPrice(Long userId, Double price, String ip, Long rating) {
        if (userId != null){
            this.user = new User(userId);
        }
        if (price != null){
            this.price = price;
        }
        if (ip != null){
            this.ip = ip;
        }
        this.rating = rating;
    }
}

