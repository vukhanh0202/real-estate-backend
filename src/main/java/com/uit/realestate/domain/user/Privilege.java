package com.uit.realestate.domain.user;

import com.uit.realestate.constant.enums.EPrivilegeType;
import com.uit.realestate.domain.SqlEntity;
import com.uit.realestate.domain.user.join.RolePrivilege;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "privilege")
public class Privilege extends SqlEntity {

    @Id
    @Enumerated(EnumType.STRING)
    private EPrivilegeType id;

    @Column(columnDefinition="TEXT")
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "privilege")
    private Set<RolePrivilege> rolePrivileges = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Privilege privilege = (Privilege) o;
        return id == privilege.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Privilege(EPrivilegeType id) {
        this.id=id;
    }
}
