package com.uit.realestate.domain.apartment.join;

import com.uit.realestate.domain.SqlEntity;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.domain.apartment.Tag;
import com.uit.realestate.domain.apartment.join.id.ApartmentTagId;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Table(name = "apartment_tag")
public class ApartmentTag extends SqlEntity {

    @EmbeddedId
    private ApartmentTagId id = new ApartmentTagId();

    @MapsId("apartmentId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Apartment apartment;

    @MapsId("tagCode")
    @ManyToOne(fetch = FetchType.LAZY)
    private Tag tag;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ApartmentTag apartmentTag = (ApartmentTag) o;
        return Objects.equals(id, apartmentTag.id) && Objects.equals(apartment, apartmentTag.apartment) && Objects.equals(tag, apartmentTag.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, apartment, tag);
    }
}
