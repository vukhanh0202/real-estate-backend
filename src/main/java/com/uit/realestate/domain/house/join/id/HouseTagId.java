package com.uit.realestate.domain.house.join.id;

import com.uit.realestate.constant.enums.EPrivilegeType;
import com.uit.realestate.constant.enums.ERoleType;
import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class HouseTagId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long houseId;
    private String tagCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HouseTagId that = (HouseTagId) o;
        return Objects.equals(houseId, that.houseId) && Objects.equals(tagCode, that.tagCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(houseId, tagCode);
    }
}
