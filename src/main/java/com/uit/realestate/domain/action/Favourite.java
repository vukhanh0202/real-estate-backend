package com.uit.realestate.domain.action;

import com.uit.realestate.domain.SqlEntity;
import com.uit.realestate.domain.action.id.ApartmentUserId;
import com.uit.realestate.domain.apartment.Apartment;
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
    private ApartmentUserId id = new ApartmentUserId();

    @MapsId("apartmentId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Apartment apartment;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
