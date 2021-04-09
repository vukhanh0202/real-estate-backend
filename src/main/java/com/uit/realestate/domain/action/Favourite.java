package com.uit.realestate.domain.action;

import com.uit.realestate.domain.SqlEntity;
import com.uit.realestate.domain.action.id.HouseUserId;
import com.uit.realestate.domain.house.House;
import com.uit.realestate.domain.house.Tag;
import com.uit.realestate.domain.house.join.id.HouseTagId;
import com.uit.realestate.domain.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "favourite")
public class Favourite extends SqlEntity {

    @EmbeddedId
    private HouseUserId id = new HouseUserId();

    @MapsId("houseId")
    @ManyToOne(fetch = FetchType.LAZY)
    private House house;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
