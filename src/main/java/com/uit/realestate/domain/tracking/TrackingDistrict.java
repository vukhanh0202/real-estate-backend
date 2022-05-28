package com.uit.realestate.domain.tracking;

import com.uit.realestate.domain.SqlEntity;
import com.uit.realestate.domain.location.Country;
import com.uit.realestate.domain.location.District;
import com.uit.realestate.domain.location.Province;
import com.uit.realestate.domain.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tracking_district")
public class TrackingDistrict extends SqlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id")
    private District district;

    @Column
    private String ip;

    @Column
    private Long rating = 0L;

    public TrackingDistrict(Long userId, District district, String ip, Long rating) {
        if (userId != null && userId != -1){
            this.user = new User(userId);
        }
        if (district != null){
            this.district = district;
        }
        if (ip != null){
            this.ip = ip;
        }
        this.rating = rating;
    }
}

