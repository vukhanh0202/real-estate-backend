package com.uit.realestate.domain.action;

import com.uit.realestate.domain.SqlEntity;
import com.uit.realestate.domain.house.House;
import com.uit.realestate.domain.user.Role;
import com.uit.realestate.domain.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "comment")
public class Comment extends SqlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id")
    private House house;

    @Column(columnDefinition = "TEXT")
    private String description;
}
