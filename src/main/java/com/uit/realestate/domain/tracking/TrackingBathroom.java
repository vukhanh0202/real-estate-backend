package com.uit.realestate.domain.tracking;

import com.uit.realestate.domain.SqlEntity;
import com.uit.realestate.domain.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "tracking_bathroom")
public class TrackingBathroom extends SqlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private Integer bathroom;

    @Column
    private String ip;

    @Column
    private Long rating = 0L;

    public TrackingBathroom(Long userId, Integer bathroom, String ip, Long rating) {
        if (userId != null && userId != -1){
            this.user = new User(userId);
        }
        if (bathroom != null){
            this.bathroom = bathroom;
        }
        if (ip != null){
            this.ip = ip;
        }
        this.rating = rating;
    }
}

