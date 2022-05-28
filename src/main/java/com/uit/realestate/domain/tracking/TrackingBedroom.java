package com.uit.realestate.domain.tracking;

import com.uit.realestate.domain.SqlEntity;
import com.uit.realestate.domain.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "tracking_bedroom")
public class TrackingBedroom extends SqlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private Integer bedroom;

    @Column
    private String ip;

    @Column
    private Long rating = 0L;

    public TrackingBedroom(Long userId, Integer bedroom, String ip, Long rating) {
        if (userId != null && userId != -1){
            this.user = new User(userId);
        }
        if (bedroom != null){
            this.bedroom = bedroom;
        }
        if (ip != null){
            this.ip = ip;
        }
        this.rating = rating;
    }
}

