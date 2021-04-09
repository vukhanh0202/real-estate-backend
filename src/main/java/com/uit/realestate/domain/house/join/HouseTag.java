package com.uit.realestate.domain.house.join;

import com.uit.realestate.domain.SqlEntity;
import com.uit.realestate.domain.house.House;
import com.uit.realestate.domain.house.Tag;
import com.uit.realestate.domain.house.join.id.HouseTagId;
import com.uit.realestate.domain.user.Privilege;
import com.uit.realestate.domain.user.Role;
import com.uit.realestate.domain.user.join.id.RolePrivilegeId;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Table(name = "house_tag")
public class HouseTag extends SqlEntity {

    @EmbeddedId
    private HouseTagId id = new HouseTagId();

    @MapsId("houseId")
    @ManyToOne(fetch = FetchType.LAZY)
    private House house;

    @MapsId("tagCode")
    @ManyToOne(fetch = FetchType.LAZY)
    private Tag tag;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        HouseTag houseTag = (HouseTag) o;
        return Objects.equals(id, houseTag.id) && Objects.equals(house, houseTag.house) && Objects.equals(tag, houseTag.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, house, tag);
    }
}
