package com.uit.realestate.domain.user.join.id;

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
public class RolePrivilegeId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Enumerated(EnumType.STRING)
    private ERoleType roleId;
    @Enumerated(EnumType.STRING)
    private EPrivilegeType privilegeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RolePrivilegeId that = (RolePrivilegeId) o;
        return Objects.equals(roleId, that.roleId) &&
                Objects.equals(privilegeId, that.privilegeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, privilegeId);
    }

    public RolePrivilegeId() {
    }

    public RolePrivilegeId(ERoleType roleId, EPrivilegeType privilegeId) {
        this.roleId = roleId;
        this.privilegeId = privilegeId;
    }

}