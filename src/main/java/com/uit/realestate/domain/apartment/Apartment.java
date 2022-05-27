package com.uit.realestate.domain.apartment;

import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.domain.SqlEntity;
import com.uit.realestate.domain.action.Favourite;
import com.uit.realestate.domain.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Entity
@Table(name = "apartment", schema = "public")
@Data
public class Apartment extends SqlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private Double area;

    private Double price;

    private Double totalPrice;

    private Double priceRent;

    private String unitRent;

    private Boolean highlight = false;

    @Column(columnDefinition = "TEXT")
    private String photos;

    @Enumerated(EnumType.STRING)
    private ETypeApartment typeApartment;

    @Enumerated(EnumType.STRING)
    private EApartmentStatus status = EApartmentStatus.PENDING;

    @OneToOne(mappedBy = "apartment",cascade = CascadeType.ALL,
            orphanRemoval = true,fetch = FetchType.LAZY)
    private ApartmentAddress apartmentAddress;

    public void setApartmentAddress(ApartmentAddress apartmentAddress) {
        if (apartmentAddress == null) {
            if (this.apartmentAddress != null) {
                this.apartmentAddress.setApartment(null);
            }
        }
        else {
            apartmentAddress.setApartment(this);
        }
        this.apartmentAddress = apartmentAddress;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true, mappedBy = "apartment")
    private List<Favourite> favourites = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @OneToOne(mappedBy = "apartment",cascade = CascadeType.ALL,
            orphanRemoval = true,fetch = FetchType.LAZY)
    private ApartmentDetail apartmentDetail;

    @Column(updatable=false, insertable=false)
    private Double suitableRate;

    public void setApartmentDetail(ApartmentDetail apartmentDetail) {
        if (apartmentDetail == null) {
            if (this.apartmentDetail != null) {
                this.apartmentDetail.setApartment(null);
            }
        }
        else {
            apartmentDetail.setApartment(this);
        }
        this.apartmentDetail = apartmentDetail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Apartment apartment = (Apartment) o;
        return Objects.equals(id, apartment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

}
