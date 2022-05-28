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
@Table(name = "tracking_floor")
public class TrackingFloor extends SqlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private Integer floor;

    @Column
    private String ip;

    @Column
    private Long rating = 0L;

    public TrackingFloor(Long userId, Integer floor, String ip, Long rating) {
        if (userId != null){
            this.user = new User(userId);
        }
        if (floor != null){
            this.floor = floor;
        }
        if (ip != null){
            this.ip = ip;
        }
        this.rating = rating;
    }
}

