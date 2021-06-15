package com.uit.realestate.domain.apartment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.domain.SqlEntity;
import com.uit.realestate.domain.action.Comment;
import com.uit.realestate.domain.action.Favourite;
import com.uit.realestate.domain.apartment.join.ApartmentTag;
import com.uit.realestate.domain.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.*;

@NoArgsConstructor
@Entity
@Table(name = "apartment")
@Data
public class Apartment extends SqlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String overview;

    private Double area;

    private Double price;

    private Double totalPrice;

    private Boolean highlight = false;

    @Column(columnDefinition = "TEXT")
    private String photos;

    @Enumerated(EnumType.STRING)
    private ETypeApartment typeApartment;

    @Enumerated(EnumType.STRING)
    private EApartmentStatus status = EApartmentStatus.PENDING;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING, timezone = "UTC")
    private Date expiredDate;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "apartment")
    private Set<ApartmentTag> apartmentTags = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "apartment")
    private List<Comment> comments = new ArrayList<>();

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
}
