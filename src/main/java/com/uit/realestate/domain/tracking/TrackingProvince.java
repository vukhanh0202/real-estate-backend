package com.uit.realestate.domain.tracking;

import com.uit.realestate.domain.SqlEntity;
import com.uit.realestate.domain.apartment.Category;
import com.uit.realestate.domain.location.Province;
import com.uit.realestate.domain.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tracking_province")
public class TrackingProvince extends SqlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id")
    private Province province;

    @Column
    private String ip;

    @Column
    private Long rating = 0L;

    public TrackingProvince(Long userId, Long provinceId, String ip, Long rating) {
        if (userId != null){
            this.user = new User(userId);
        }
        if (provinceId != null){
            this.province = new Province(provinceId);
        }
        if (ip != null){
            this.ip = ip;
        }
        this.rating = rating;
    }
}

