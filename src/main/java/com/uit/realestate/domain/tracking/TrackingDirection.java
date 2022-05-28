package com.uit.realestate.domain.tracking;

import com.uit.realestate.domain.SqlEntity;
import com.uit.realestate.domain.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "tracking_direction")
public class TrackingDirection extends SqlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String direction;

    @Column
    private String ip;

    @Column
    private Long rating = 0L;

    public TrackingDirection(Long userId, String direction, String ip, Long rating) {
        if (userId != null){
            this.user = new User(userId);
        }
        if (direction != null){
            this.direction = direction;
        }
        if (ip != null){
            this.ip = ip;
        }
        this.rating = rating;
    }
}

