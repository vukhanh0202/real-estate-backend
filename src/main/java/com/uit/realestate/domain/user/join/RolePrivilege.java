package com.uit.realestate.domain.user.join;

import com.uit.realestate.constant.enums.user.EPrivilegeType;
import com.uit.realestate.domain.SqlEntity;
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
@Table(name = "role_privilege")
public class RolePrivilege extends SqlEntity {

    @EmbeddedId
    private RolePrivilegeId id = new RolePrivilegeId();

    @MapsId("roleId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Role role;

    @MapsId("privilegeId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Privilege privilege;

    @Enumerated(EnumType.STRING)
    @Column(name="privilege_id", nullable=false, insertable = false,updatable = false)
    private EPrivilegeType privilegeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RolePrivilege that = (RolePrivilege) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public RolePrivilege(RolePrivilegeId id) {
        this.id = id;
    }

}
