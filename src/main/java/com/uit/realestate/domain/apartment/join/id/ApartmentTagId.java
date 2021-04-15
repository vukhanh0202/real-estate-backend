package com.uit.realestate.domain.apartment.join.id;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class ApartmentTagId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long apartmentId;
    private String tagCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApartmentTagId that = (ApartmentTagId) o;
        return Objects.equals(apartmentId, that.apartmentId) && Objects.equals(tagCode, that.tagCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apartmentId, tagCode);
    }
}
