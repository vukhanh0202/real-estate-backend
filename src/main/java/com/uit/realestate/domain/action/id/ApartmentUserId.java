package com.uit.realestate.domain.action.id;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class ApartmentUserId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long apartmentId;
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApartmentUserId that = (ApartmentUserId) o;
        return Objects.equals(apartmentId, that.apartmentId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apartmentId, userId);
    }
}
