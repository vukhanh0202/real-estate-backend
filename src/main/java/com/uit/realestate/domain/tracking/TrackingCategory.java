package com.uit.realestate.domain.tracking;

import com.uit.realestate.domain.SqlEntity;
import com.uit.realestate.domain.apartment.Category;
import com.uit.realestate.domain.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tracking_category")
public class TrackingCategory extends SqlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column
    private String ip;

    @Column
    private Long rating = 0L;

    public TrackingCategory(Long userId, Category category, String ip, Long rating) {
        if (userId != null && userId != -1){
            this.user = new User(userId);
        }
        if (category != null){
            this.category = category;
        }
        if (ip != null){
            this.ip = ip;
        }
        this.rating = rating;
    }
}

