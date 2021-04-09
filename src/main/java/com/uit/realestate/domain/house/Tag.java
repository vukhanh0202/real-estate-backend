package com.uit.realestate.domain.house;

import com.uit.realestate.domain.SqlEntity;
import com.uit.realestate.domain.house.join.HouseTag;
import com.uit.realestate.domain.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Entity
@Table(name = "tag")
@Data
public class Tag extends SqlEntity {

    @Id
    @Column
    private String code;

    @Column
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tag")
    private Set<HouseTag> houseTags = new HashSet<>();
}
