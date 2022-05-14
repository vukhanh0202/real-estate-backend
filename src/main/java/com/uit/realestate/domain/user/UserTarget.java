package com.uit.realestate.domain.user;

import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.domain.SqlEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_target")
public class UserTarget extends SqlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Long district;

    private Long province;

    private Double price;

    private Long floorQuantity;

    private Long bedroomQuantity;

    private Long bathroomQuantity;

    private Double area;

    private Long category;

    @Enumerated(EnumType.STRING)
    private ETypeApartment typeApartment;

    @Override
    public String toString() {
        return "UserTarget{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserTarget that = (UserTarget) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
